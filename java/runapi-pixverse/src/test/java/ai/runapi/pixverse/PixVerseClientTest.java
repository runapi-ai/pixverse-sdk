package ai.runapi.pixverse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ai.runapi.core.RequestOptions;
import ai.runapi.core.billing.TaskBillingFacts;
import ai.runapi.core.errors.ValidationException;
import ai.runapi.core.http.HttpRequest;
import ai.runapi.core.http.HttpResponse;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.http.JsonRequestBody;
import ai.runapi.core.json.Json;
import ai.runapi.pixverse.types.CompletedTextToVideoResponse;
import ai.runapi.pixverse.types.TextToVideoResponse;
import ai.runapi.pixverse.types.CompletedEditVideoResponse;
import ai.runapi.pixverse.types.CompletedExtendVideoResponse;
import ai.runapi.pixverse.types.CompletedImageToVideoResponse;
import ai.runapi.pixverse.types.CompletedTextToVideoResponse;
import ai.runapi.pixverse.types.CompletedTransitionVideoResponse;
import ai.runapi.pixverse.types.EditVideoModel;
import ai.runapi.pixverse.types.EditVideoParams;
import ai.runapi.pixverse.types.EditVideoResponse;
import ai.runapi.pixverse.types.ExtendVideoModel;
import ai.runapi.pixverse.types.ExtendVideoParams;
import ai.runapi.pixverse.types.ExtendVideoResponse;
import ai.runapi.pixverse.types.ImageToVideoModel;
import ai.runapi.pixverse.types.ImageToVideoParams;
import ai.runapi.pixverse.types.ImageToVideoResponse;
import ai.runapi.pixverse.types.TextToVideoModel;
import ai.runapi.pixverse.types.TextToVideoParams;
import ai.runapi.pixverse.types.TextToVideoResponse;
import ai.runapi.pixverse.types.TransitionVideoModel;
import ai.runapi.pixverse.types.TransitionVideoParams;
import ai.runapi.pixverse.types.TransitionVideoResponse;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class PixVerseClientTest {
  @Test
  void builderCreatesClientAndUniversalResources() {
    PixVerseClient client = PixVerseClient.builder().apiKey("sk-test").build();

    assertNotNull(client.textToVideo());
    assertNotNull(client.files());
    assertNotNull(client.account());
    assertNotNull(client.pricing());
  }

  @Test
  void openValueClassesSerializeAsScalarStrings() throws Exception {
    String json = Json.mapper().writeValueAsString(new TextToVideoModel("pixverse-v6"));

    assertEquals("\"pixverse-v6\"", json);
    assertEquals(new TextToVideoModel("pixverse-v6"), Json.mapper().readValue(json, TextToVideoModel.class));
  }

  @Test
  void createSendsExpectedRequestShape() throws Exception {
    CapturingTransport transport = new CapturingTransport("{\"id\":\"task_123\",\"status\":\"processing\"}");
    PixVerseClient client = PixVerseClient.builder().apiKey("sk-test").transport(transport).build();

    client.textToVideo().create(
        TextToVideoParams.builder()
            .model(TextToVideoModel.PIXVERSE_V6)
            .prompt("A small red cube on a plain white table, studio product photo")
            .outputResolution("360p")
            .durationSeconds(1)
            .aspectRatio("16:9")
            .build()
    );

    assertEquals("POST", transport.request.getMethod().name());
    assertEquals("/api/v1/pixverse/text_to_video", transport.request.getPath());
    JsonNode body = bodyJson(transport.request);
    assertNotNull(body);

  }

  @Test
  void getDecodesTaskResponseAndExtraFields() {
    CapturingTransport transport = new CapturingTransport("{\"id\":\"task_456\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}],\"billing\":{\"reservation\":{\"amount_cents\":12},\"settlement\":{\"charged_amount_cents\":11,\"amount_micro_cents\":1050000},\"refund\":{\"refunded_at\":\"2026-07-23T12:00:00.000000Z\"}},\"custom\":\"kept\"}");
    PixVerseClient client = PixVerseClient.builder().apiKey("sk-test").transport(transport).build();

    TextToVideoResponse response = client.textToVideo().get("task_456");

    assertEquals("GET", transport.request.getMethod().name());
    assertEquals("/api/v1/pixverse/text_to_video/task_456", transport.request.getPath());
    assertEquals("completed", response.getStatus().value());
    assertNotNull(response.getVideos());
    assertEquals("kept", response.extraFields().get("custom").asText());
    TaskBillingFacts billing = response.getBilling();
    assertNotNull(billing);
    assertEquals(Long.valueOf(12), billing.getReservation().getAmountCents());
    assertEquals(Long.valueOf(11), billing.getSettlement().getChargedAmountCents());
    assertEquals(Long.valueOf(1050000), billing.getSettlement().getAmountMicroCents());
    assertEquals("2026-07-23T12:00:00.000000Z", billing.getRefund().getRefundedAt());
  }

  @Test
  void runPollsUntilCompletedAndKeepsExtraFields() {
    SequenceTransport transport = new SequenceTransport(
        "{\"id\":\"task_789\",\"status\":\"processing\"}",
        "{\"id\":\"task_789\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}],\"custom\":\"kept\"}");
    PixVerseClient client = PixVerseClient.builder().apiKey("sk-test").transport(transport).build();

    CompletedTextToVideoResponse response = client.textToVideo().run(
        TextToVideoParams.builder()
            .model(TextToVideoModel.PIXVERSE_V6)
            .prompt("A small red cube on a plain white table, studio product photo")
            .outputResolution("360p")
            .durationSeconds(1)
            .aspectRatio("16:9")
            .build(),
        RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());

    assertEquals("completed", response.getStatus().value());
    assertNotNull(response.getVideos());
    assertEquals("kept", response.extraFields().get("custom").asText());
    assertEquals(2, transport.calls);
  }

  @Test
  void runRejectsCompletedResponseMissingResultField() {
    SequenceTransport transport = new SequenceTransport(
        "{\"id\":\"task_missing\",\"status\":\"processing\"}",
        "{\"id\":\"task_missing\",\"status\":\"completed\"}");
    PixVerseClient client = PixVerseClient.builder().apiKey("sk-test").transport(transport).build();

    assertThrows(
        ValidationException.class,
        () -> client.textToVideo().run(
                TextToVideoParams.builder()
                    .model(TextToVideoModel.PIXVERSE_V6)
                    .prompt("A small red cube on a plain white table, studio product photo")
                    .outputResolution("360p")
                    .durationSeconds(1)
                    .aspectRatio("16:9")
                    .build(),
            RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
  }

    @Test
    void coversEditvideoResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_edit_video\",\"status\":\"processing\"}");
      PixVerseClient createClient = PixVerseClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.editVideo().create(
              EditVideoParams.builder()
                  .model(EditVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .referenceImageUrls(java.util.Arrays.asList("https://cdn.runapi.ai/public/samples/image.jpg"))
                  .aspectRatio("16:9")
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_edit_video_options\",\"status\":\"processing\"}");
      PixVerseClient createWithOptionsClient = PixVerseClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.editVideo().create(
              EditVideoParams.builder()
                  .model(EditVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .referenceImageUrls(java.util.Arrays.asList("https://cdn.runapi.ai/public/samples/image.jpg"))
                  .aspectRatio("16:9")
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_edit_video\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient getClient = PixVerseClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.editVideo().get("task_edit_video"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_edit_video_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient getWithOptionsClient = PixVerseClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.editVideo().get("task_edit_video_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_edit_video_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_edit_video_run\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient runClient = PixVerseClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedEditVideoResponse runResponse = runClient.editVideo().run(
              EditVideoParams.builder()
                  .model(EditVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .referenceImageUrls(java.util.Arrays.asList("https://cdn.runapi.ai/public/samples/image.jpg"))
                  .aspectRatio("16:9")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_edit_video_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_edit_video_run_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient runWithOptionsClient = PixVerseClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.editVideo().run(
              EditVideoParams.builder()
                  .model(EditVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .referenceImageUrls(java.util.Arrays.asList("https://cdn.runapi.ai/public/samples/image.jpg"))
                  .aspectRatio("16:9")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

    @Test
    void coversExtendvideoResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_extend_video\",\"status\":\"processing\"}");
      PixVerseClient createClient = PixVerseClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.extendVideo().create(
              ExtendVideoParams.builder()
                  .model(ExtendVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .sourceTaskId("sample")
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_extend_video_options\",\"status\":\"processing\"}");
      PixVerseClient createWithOptionsClient = PixVerseClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.extendVideo().create(
              ExtendVideoParams.builder()
                  .model(ExtendVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .sourceTaskId("sample")
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_extend_video\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient getClient = PixVerseClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.extendVideo().get("task_extend_video"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_extend_video_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient getWithOptionsClient = PixVerseClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.extendVideo().get("task_extend_video_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_extend_video_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_extend_video_run\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient runClient = PixVerseClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedExtendVideoResponse runResponse = runClient.extendVideo().run(
              ExtendVideoParams.builder()
                  .model(ExtendVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .sourceTaskId("sample")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_extend_video_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_extend_video_run_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient runWithOptionsClient = PixVerseClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.extendVideo().run(
              ExtendVideoParams.builder()
                  .model(ExtendVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .sourceTaskId("sample")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

    @Test
    void coversImagetovideoResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_image_to_video\",\"status\":\"processing\"}");
      PixVerseClient createClient = PixVerseClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.imageToVideo().create(
              ImageToVideoParams.builder()
                  .model(ImageToVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .firstFrameImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_image_to_video_options\",\"status\":\"processing\"}");
      PixVerseClient createWithOptionsClient = PixVerseClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.imageToVideo().create(
              ImageToVideoParams.builder()
                  .model(ImageToVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .firstFrameImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_image_to_video\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient getClient = PixVerseClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.imageToVideo().get("task_image_to_video"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_image_to_video_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient getWithOptionsClient = PixVerseClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.imageToVideo().get("task_image_to_video_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_image_to_video_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_image_to_video_run\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient runClient = PixVerseClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedImageToVideoResponse runResponse = runClient.imageToVideo().run(
              ImageToVideoParams.builder()
                  .model(ImageToVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .firstFrameImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_image_to_video_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_image_to_video_run_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient runWithOptionsClient = PixVerseClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.imageToVideo().run(
              ImageToVideoParams.builder()
                  .model(ImageToVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .firstFrameImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

    @Test
    void coversTexttovideoResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_text_to_video\",\"status\":\"processing\"}");
      PixVerseClient createClient = PixVerseClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.textToVideo().create(
              TextToVideoParams.builder()
                  .model(TextToVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .aspectRatio("16:9")
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_text_to_video_options\",\"status\":\"processing\"}");
      PixVerseClient createWithOptionsClient = PixVerseClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.textToVideo().create(
              TextToVideoParams.builder()
                  .model(TextToVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .aspectRatio("16:9")
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_text_to_video\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient getClient = PixVerseClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.textToVideo().get("task_text_to_video"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_text_to_video_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient getWithOptionsClient = PixVerseClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.textToVideo().get("task_text_to_video_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_text_to_video_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_text_to_video_run\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient runClient = PixVerseClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedTextToVideoResponse runResponse = runClient.textToVideo().run(
              TextToVideoParams.builder()
                  .model(TextToVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .aspectRatio("16:9")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_text_to_video_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_text_to_video_run_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient runWithOptionsClient = PixVerseClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.textToVideo().run(
              TextToVideoParams.builder()
                  .model(TextToVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .aspectRatio("16:9")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

    @Test
    void coversTransitionvideoResourceMethods() {
      CapturingTransport createTransport = new CapturingTransport("{\"id\":\"task_transition_video\",\"status\":\"processing\"}");
      PixVerseClient createClient = PixVerseClient.builder().apiKey("sk-test").transport(createTransport).build();
      assertNotNull(createClient.transitionVideo().create(
              TransitionVideoParams.builder()
                  .model(TransitionVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .firstFrameImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .lastFrameImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build()
      ));

      CapturingTransport createWithOptionsTransport = new CapturingTransport("{\"id\":\"task_transition_video_options\",\"status\":\"processing\"}");
      PixVerseClient createWithOptionsClient = PixVerseClient.builder().apiKey("sk-test").transport(createWithOptionsTransport).build();
      assertNotNull(createWithOptionsClient.transitionVideo().create(
              TransitionVideoParams.builder()
                  .model(TransitionVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .firstFrameImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .lastFrameImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.none()));

      CapturingTransport getTransport = new CapturingTransport("{\"id\":\"task_transition_video\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient getClient = PixVerseClient.builder().apiKey("sk-test").transport(getTransport).build();
      assertNotNull(getClient.transitionVideo().get("task_transition_video"));

      CapturingTransport getWithOptionsTransport = new CapturingTransport("{\"id\":\"task_transition_video_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient getWithOptionsClient = PixVerseClient.builder().apiKey("sk-test").transport(getWithOptionsTransport).build();
      assertNotNull(getWithOptionsClient.transitionVideo().get("task_transition_video_options", RequestOptions.none()));

      SequenceTransport runTransport = new SequenceTransport(
          "{\"id\":\"task_transition_video_run\",\"status\":\"processing\"}",
          "{\"id\":\"task_transition_video_run\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient runClient = PixVerseClient.builder().apiKey("sk-test").transport(runTransport).build();
      CompletedTransitionVideoResponse runResponse = runClient.transitionVideo().run(
              TransitionVideoParams.builder()
                  .model(TransitionVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .firstFrameImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .lastFrameImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build());
      assertNotNull(runResponse);

      SequenceTransport runWithOptionsTransport = new SequenceTransport(
          "{\"id\":\"task_transition_video_run_options\",\"status\":\"processing\"}",
          "{\"id\":\"task_transition_video_run_options\",\"status\":\"completed\",\"videos\":[{\"url\":\"https://file.runapi.ai/generated\"}]}");
      PixVerseClient runWithOptionsClient = PixVerseClient.builder().apiKey("sk-test").transport(runWithOptionsTransport).build();
      assertNotNull(runWithOptionsClient.transitionVideo().run(
              TransitionVideoParams.builder()
                  .model(TransitionVideoModel.PIXVERSE_V6)
                  .prompt("A small red cube on a plain white table, studio product photo")
                  .outputResolution("360p")
                  .durationSeconds(1)
                  .firstFrameImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .lastFrameImageUrl("https://cdn.runapi.ai/public/samples/image.jpg")
                  .build(),
          RequestOptions.builder().pollingInterval(Duration.ofMillis(1)).pollingMaxWait(Duration.ofSeconds(1)).build()));
    }

  private static JsonNode bodyJson(HttpRequest request) throws Exception {
    JsonRequestBody body = (JsonRequestBody) request.getBody();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    body.writeTo(out);
    return Json.mapper().readTree(out.toByteArray());
  }

  private static final class CapturingTransport implements HttpTransport {
    private final String body;
    private HttpRequest request;

    private CapturingTransport(String body) {
      this.body = body;
    }

    public HttpResponse send(HttpRequest request) {
      this.request = request;
      return new HttpResponse(200, body, Collections.<String, java.util.List<String>>emptyMap());
    }

    public void close() {}
  }

  private static final class SequenceTransport implements HttpTransport {
    private final String[] responses;
    private int calls;

    private SequenceTransport(String... responses) {
      this.responses = responses;
    }

    public HttpResponse send(HttpRequest request) {
      String response = responses[Math.min(calls, responses.length - 1)];
      calls++;
      return new HttpResponse(200, response, Collections.<String, java.util.List<String>>emptyMap());
    }

    public void close() {}
  }
}
