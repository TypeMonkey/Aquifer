package jg.aquifer.ui.value;

import jg.aquifer.commands.options.ExclusiveOptions;
import jg.aquifer.commands.options.Option;
import jg.aquifer.commands.options.VerificationException;

public class ExclusiveValueStatus extends ValueStatus {

  private final Option targetOption;
  private final String actualValue;

  public ExclusiveValueStatus(ExclusiveOptions option, Option target, String value, VerificationException exception) {
    super(option, target.getOptName()+":"+value, exception);
    this.targetOption = target;
    this.actualValue = value;
  }
  
  public boolean isForOption(Option target) {
    return targetOption.equals(target);
  }

  @Override
  public boolean isVerified() {
    return super.isVerified() && actualValue != null;
  }
}
