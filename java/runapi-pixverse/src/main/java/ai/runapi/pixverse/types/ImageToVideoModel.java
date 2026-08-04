package ai.runapi.pixverse.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for image to video operations. */
public final class ImageToVideoModel extends PixverseValue {
  /** pixverse-v6 model slug. */
  public static final ImageToVideoModel PIXVERSE_V6 = new ImageToVideoModel("pixverse-v6");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public ImageToVideoModel(String value) {
    super(value);
  }
}
