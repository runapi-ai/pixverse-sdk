package ai.runapi.pixverse.resources;

import ai.runapi.core.ClientOptions;
import ai.runapi.core.RequestOptions;
import ai.runapi.core.http.HttpTransport;
import ai.runapi.core.polling.TaskCreateResponse;
import ai.runapi.pixverse.types.CompletedTransitionVideoResponse;
import ai.runapi.pixverse.types.TransitionVideoParams;
import ai.runapi.pixverse.types.TransitionVideoResponse;

/** Transition Video operations. */
public final class TransitionVideoResource extends PixverseResource {
  /** API endpoint path for transition video operations. */
  public static final String ENDPOINT = "/api/v1/pixverse/transition_video";

  /** Creates a resource bound to the supplied transport and client options. */
  public TransitionVideoResource(HttpTransport transport, ClientOptions options) {
    super(transport, options, ENDPOINT);
  }

  /** Creates a transition video task. */
  public TaskCreateResponse create(TransitionVideoParams params) {
    return create(params, RequestOptions.none());
  }

  /** Creates a transition video task with per-request options. */
  public TaskCreateResponse create(TransitionVideoParams params, RequestOptions options) {
    return createTask(params.action(), params.toMap(), options);
  }

  /** Retrieves a transition video task by ID. */
  public TransitionVideoResponse get(String id) {
    return get(id, RequestOptions.none());
  }

  /** Retrieves a transition video task by ID with per-request options. */
  public TransitionVideoResponse get(String id, RequestOptions options) {
    return getTask(id, options, TransitionVideoResponse.class);
  }

  /** Creates a transition video task and polls until it completes. */
  public CompletedTransitionVideoResponse run(TransitionVideoParams params) {
    return run(params, RequestOptions.none());
  }

  /** Creates a transition video task with per-request options and polls until it completes. */
  public CompletedTransitionVideoResponse run(TransitionVideoParams params, RequestOptions options) {
    return runTask(params.action(), params.toMap(), options, TransitionVideoResponse.class, CompletedTransitionVideoResponse.class);
  }
}
