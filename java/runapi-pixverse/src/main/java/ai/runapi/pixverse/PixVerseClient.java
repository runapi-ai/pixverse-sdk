package ai.runapi.pixverse;

import ai.runapi.core.BaseClient;
import ai.runapi.core.ClientOptions;
import ai.runapi.core.http.HttpTransport;
import java.net.URI;
import ai.runapi.pixverse.resources.EditVideoResource;
import ai.runapi.pixverse.resources.ExtendVideoResource;
import ai.runapi.pixverse.resources.ImageToVideoResource;
import ai.runapi.pixverse.resources.TextToVideoResource;
import ai.runapi.pixverse.resources.TransitionVideoResource;

/** PixVerse model-family Java SDK client. */
public final class PixVerseClient extends BaseClient {
  private final EditVideoResource editVideo;
  private final ExtendVideoResource extendVideo;
  private final ImageToVideoResource imageToVideo;
  private final TextToVideoResource textToVideo;
  private final TransitionVideoResource transitionVideo;

  private PixVerseClient(ClientOptions options) {
    super(options);
    this.editVideo = new EditVideoResource(transport(), options());
    this.extendVideo = new ExtendVideoResource(transport(), options());
    this.imageToVideo = new ImageToVideoResource(transport(), options());
    this.textToVideo = new TextToVideoResource(transport(), options());
    this.transitionVideo = new TransitionVideoResource(transport(), options());
  }

  /** Creates a new PixVerseClient builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Edit Video operations. */
  public EditVideoResource editVideo() {
    return editVideo;
  }

  /** Extend Video operations. */
  public ExtendVideoResource extendVideo() {
    return extendVideo;
  }

  /** Image To Video operations. */
  public ImageToVideoResource imageToVideo() {
    return imageToVideo;
  }

  /** Text To Video operations. */
  public TextToVideoResource textToVideo() {
    return textToVideo;
  }

  /** Transition Video operations. */
  public TransitionVideoResource transitionVideo() {
    return transitionVideo;
  }

  /** Builder for {@link PixVerseClient}. */
  public static final class Builder extends BaseClient.Builder<Builder> {
    private Builder() {}

    /** Sets the API key. If omitted, the SDK reads {@code RUNAPI_API_KEY}. */
    @Override
    public Builder apiKey(String value) {
      return super.apiKey(value);
    }

    /** Sets the RunAPI base URL. If omitted, the SDK reads {@code RUNAPI_BASE_URL}. */
    @Override
    public Builder baseUrl(String value) {
      return super.baseUrl(value);
    }

    /** Sets the RunAPI base URL from a URI. */
    @Override
    public Builder baseUrl(URI value) {
      return super.baseUrl(value);
    }

    /** Sets a custom HTTP transport. User-provided transports are not closed by SDK clients. */
    @Override
    public Builder transport(HttpTransport value) {
      return super.transport(value);
    }

    /** Builds an immutable PixVerseClient. */
    @Override
    public PixVerseClient build() {
      return new PixVerseClient(options.build());
    }
  }
}
