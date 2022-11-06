package jg.aquifer.ui.value;

import jg.aquifer.commands.options.ExclusiveOptions;
import jg.aquifer.commands.options.Option;
import jg.aquifer.commands.options.VerificationException;

/**
 * Wraps the argument for a ExclusiveValueStatus and it's verified status
 * 
 * The verification status of a ExclusiveValueStatus is solely
 * determined on whether the nested ValueStatus is verified 
 * 
 * @author Jose
 */
public class ExclusiveValueStatus extends ValueStatus {

  private final ValueStatus actualValue;

  /**
   * Constructs an ExclusiveValueStatus
   * @param option - the ExclusiveOptions this status represents
   * @param actualValue - the ValueStatus of the Option that was chosen from the ExclusiveOption
   * @param exception - the VerificationException to be attached with this status
   */
  public ExclusiveValueStatus(ExclusiveOptions option, ValueStatus actualValue, VerificationException exception) {
    super(option, actualValue.getOption().getOptName()+":"+actualValue.getValue(), exception);
    this.actualValue = actualValue;
  }
  
  /**
   * Checks if the given Option is what was chosen from 
   * the ExclusiveOptions
   * @param target - the Option to check for
   * @return true if the nested ValueStatus' Option is the target, false if else.
   */
  public boolean isForOption(Option target) {
    return actualValue.getOption().equals(target);
  }

  @Override
  public boolean isVerified() {
    return actualValue.isVerified();
  }
}
