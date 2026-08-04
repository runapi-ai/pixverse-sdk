package ai.runapi.pixverse.resources;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.polling.TaskCreateResponse;
import ai.runapi.pixverse.types.CompletedEditVideoResponse;
import ai.runapi.pixverse.types.EditVideoParams;
import ai.runapi.pixverse.types.EditVideoResponse;

/** Edit Video operations. */
public final class EditVideoResource extends PixverseResource {
  /** API endpoint path for edit video operations. */
  public static final String ENDPOINT = "/api/v1/pixverse/edit_video";

  /** Creates a resource bound to the supplied transport and client options. */
  public EditVideoResource(HttpTransport transport, ClientOptions options) {
    super(transport, options, ENDPOINT);
  }

  /** Creates a edit video task. */
  public TaskCreateResponse create(EditVideoParams params) {
    return create(params, RequestOptions.none());
  }

  /** Creates a edit video task with per-request options. */
  public TaskCreateResponse create(EditVideoParams params, RequestOptions options) {
    return createTask(params.action(), params.toMap(), options);
  }

  /** Retrieves a edit video task by ID. */
  public EditVideoResponse get(String id) {
    return get(id, RequestOptions.none());
  }

  /** Retrieves a edit video task by ID with per-request options. */
  public EditVideoResponse get(String id, RequestOptions options) {
    return getTask(id, options, EditVideoResponse.class);
  }

  /** Creates a edit video task and polls until it completes. */
  public CompletedEditVideoResponse run(EditVideoParams params) {
    return run(params, RequestOptions.none());
  }

  /** Creates a edit video task with per-request options and polls until it completes. */
  public CompletedEditVideoResponse run(EditVideoParams params, RequestOptions options) {
    return runTask(params.action(), params.toMap(), options, EditVideoResponse.class, CompletedEditVideoResponse.class);
  }
}
