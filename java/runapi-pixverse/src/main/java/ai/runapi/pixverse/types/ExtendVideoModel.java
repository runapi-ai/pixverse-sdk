package ai.runapi.pixverse.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for extend video operations. */
public final class ExtendVideoModel extends PixverseValue {
  /** pixverse-v6 model slug. */
  public static final ExtendVideoModel PIXVERSE_V6 = new ExtendVideoModel("pixverse-v6");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public ExtendVideoModel(String value) {
    super(value);
  }
}
