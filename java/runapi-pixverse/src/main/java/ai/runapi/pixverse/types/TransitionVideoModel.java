package ai.runapi.pixverse.types;

import com.fasterxml.jackson.annotation.JsonCreator;

/** Model slug for transition video operations. */
public final class TransitionVideoModel extends PixverseValue {
  /** pixverse-v6 model slug. */
  public static final TransitionVideoModel PIXVERSE_V6 = new TransitionVideoModel("pixverse-v6");

  /** Creates a model value from a literal model slug. */
  @JsonCreator
  public TransitionVideoModel(String value) {
    super(value);
  }
}
