package ai.runapi.pixverse.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for image to video operations. */
public final class ImageToVideoParams {
  private final String model;
  private final String prompt;
  private final String outputResolution;
  private final Integer durationSeconds;
  private final Boolean enableAudio;
  private final Integer seed;
  private final String callbackUrl;
  private final String firstFrameImageUrl;
  private final Boolean enableMultiClip;

  private ImageToVideoParams(Builder builder) {
    this.model = PixverseParamUtils.requireNonBlankTrim(builder.model, "model");
    this.prompt = PixverseParamUtils.requireNonBlank(builder.prompt, "prompt");
    this.outputResolution = PixverseParamUtils.requireNonBlank(builder.outputResolution, "outputResolution");
    this.durationSeconds = java.util.Objects.requireNonNull(builder.durationSeconds, "durationSeconds");
    this.enableAudio = builder.enableAudio;
    this.seed = builder.seed;
    this.callbackUrl = builder.callbackUrl;
    this.firstFrameImageUrl = PixverseParamUtils.requireNonBlank(builder.firstFrameImageUrl, "firstFrameImageUrl");
    this.enableMultiClip = builder.enableMultiClip;
  }

  /** Creates a new ImageToVideoParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "pixverse/image-to-video";
  }

  /** Converts these parameters to the JSON request body shape. */
  public Map<String, Object> toMap() {
    Map<String, Object> raw = new LinkedHashMap<String, Object>();
    raw.put("model", PixverseParamUtils.wireValue(model));
    raw.put("prompt", PixverseParamUtils.wireValue(prompt));
    raw.put("output_resolution", PixverseParamUtils.wireValue(outputResolution));
    raw.put("duration_seconds", PixverseParamUtils.wireValue(durationSeconds));
    raw.put("enable_audio", PixverseParamUtils.wireValue(enableAudio));
    raw.put("seed", PixverseParamUtils.wireValue(seed));
    raw.put("callback_url", PixverseParamUtils.wireValue(callbackUrl));
    raw.put("first_frame_image_url", PixverseParamUtils.wireValue(firstFrameImageUrl));
    raw.put("enable_multi_clip", PixverseParamUtils.wireValue(enableMultiClip));
    return PixverseParamUtils.compact(raw);
  }



  /** Builder for {@link ImageToVideoParams}. */
  public static final class Builder {
    private String model;
    private String prompt;
    private String outputResolution;
    private Integer durationSeconds;
    private Boolean enableAudio;
    private Integer seed;
    private String callbackUrl;
    private String firstFrameImageUrl;
    private Boolean enableMultiClip;

    private Builder() {}

    /** Sets the model slug using a typed model value. */
    public Builder model(ImageToVideoModel value) {
      this.model = java.util.Objects.requireNonNull(value, "model").value();
      return this;
    }

    /** Sets the model slug using a string value. */
    public Builder model(String value) {
      this.model = PixverseParamUtils.requireNonBlankTrim(value, "model");
      return this;
    }


    /** Sets the text prompt. */
    public Builder prompt(String value) {
      this.prompt = PixverseParamUtils.requireNonBlank(value, "prompt");
      return this;
    }

    /** Sets the output resolution. */
    public Builder outputResolution(String value) {
      this.outputResolution = PixverseParamUtils.requireNonBlank(value, "outputResolution");
      return this;
    }

    /** Sets the duration in seconds. */
    public Builder durationSeconds(int value) {
      this.durationSeconds = value;
      return this;
    }

    /** Sets the enable audio. */
    public Builder enableAudio(boolean value) {
      this.enableAudio = value;
      return this;
    }

    /** Sets the random seed. */
    public Builder seed(int value) {
      this.seed = value;
      return this;
    }

    /** Sets the webhook URL for task completion notifications. */
    public Builder callbackUrl(String value) {
      this.callbackUrl = PixverseParamUtils.requireNonBlank(value, "callbackUrl");
      return this;
    }

    /** Sets the first frame image URL. */
    public Builder firstFrameImageUrl(String value) {
      this.firstFrameImageUrl = PixverseParamUtils.requireNonBlank(value, "firstFrameImageUrl");
      return this;
    }

    /** Sets the enable multi clip. */
    public Builder enableMultiClip(boolean value) {
      this.enableMultiClip = value;
      return this;
    }

    /** Builds immutable image to video parameters. */
    public ImageToVideoParams build() {
      return new ImageToVideoParams(this);
    }
  }
}
