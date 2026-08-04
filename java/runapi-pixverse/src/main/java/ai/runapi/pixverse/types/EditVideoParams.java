package ai.runapi.pixverse.types;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parameters for edit video operations. */
public final class EditVideoParams {
  private final String model;
  private final String prompt;
  private final String outputResolution;
  private final Integer durationSeconds;
  private final Boolean enableAudio;
  private final Integer seed;
  private final String callbackUrl;
  private final List<String> referenceImageUrls;
  private final String aspectRatio;

  private EditVideoParams(Builder builder) {
    this.model = PixverseParamUtils.requireNonBlankTrim(builder.model, "model");
    this.prompt = PixverseParamUtils.requireNonBlank(builder.prompt, "prompt");
    this.outputResolution = PixverseParamUtils.requireNonBlank(builder.outputResolution, "outputResolution");
    this.durationSeconds = java.util.Objects.requireNonNull(builder.durationSeconds, "durationSeconds");
    this.enableAudio = builder.enableAudio;
    this.seed = builder.seed;
    this.callbackUrl = builder.callbackUrl;
    this.referenceImageUrls = PixverseParamUtils.requiredStrings(builder.referenceImageUrls, "referenceImageUrls");
    this.aspectRatio = PixverseParamUtils.requireNonBlank(builder.aspectRatio, "aspectRatio");
  }

  /** Creates a new EditVideoParams builder. */
  public static Builder builder() {
    return new Builder();
  }

  /** Returns the RunAPI action key for this request. */
  public String action() {
    return "pixverse/edit-video";
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
    raw.put("reference_image_urls", PixverseParamUtils.wireValue(referenceImageUrls));
    raw.put("aspect_ratio", PixverseParamUtils.wireValue(aspectRatio));
    return PixverseParamUtils.compact(raw);
  }



  /** Builder for {@link EditVideoParams}. */
  public static final class Builder {
    private String model;
    private String prompt;
    private String outputResolution;
    private Integer durationSeconds;
    private Boolean enableAudio;
    private Integer seed;
    private String callbackUrl;
    private List<String> referenceImageUrls;
    private String aspectRatio;

    private Builder() {}

    /** Sets the model slug using a typed model value. */
    public Builder model(EditVideoModel value) {
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

    /** Sets the reference image URLs. */
    public Builder referenceImageUrls(List<String> value) {
      this.referenceImageUrls = value;
      return this;
    }

    /** Sets the output aspect ratio. */
    public Builder aspectRatio(String value) {
      this.aspectRatio = PixverseParamUtils.requireNonBlank(value, "aspectRatio");
      return this;
    }

    /** Builds immutable edit video parameters. */
    public EditVideoParams build() {
      return new EditVideoParams(this);
    }
  }
}
