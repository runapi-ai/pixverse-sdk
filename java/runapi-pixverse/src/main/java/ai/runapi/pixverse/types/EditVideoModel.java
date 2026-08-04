package ai.runapi.pixverse.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for edit video operations. */
public final class EditVideoModel extends PixverseValue {
  /** pixverse-v6 model slug. */
  public static final EditVideoModel PIXVERSE_V6 = new EditVideoModel("pixverse-v6");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public EditVideoModel(String value) {
    super(value);
  }
}
