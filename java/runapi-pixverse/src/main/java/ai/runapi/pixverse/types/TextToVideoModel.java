package ai.runapi.pixverse.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for text to video operations. */
public final class TextToVideoModel extends PixverseValue {
  /** pixverse-v6 model slug. */
  public static final TextToVideoModel PIXVERSE_V6 = new TextToVideoModel("pixverse-v6");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public TextToVideoModel(String value) {
    super(value);
  }
}
