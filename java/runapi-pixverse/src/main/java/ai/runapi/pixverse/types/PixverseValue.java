package ai.runapi.pixverse.types;

import ai.runapi.core.types.RunApiValue;

abstract class PixverseValue extends RunApiValue {
  PixverseValue(String value) {
    super(value);
  }
}
