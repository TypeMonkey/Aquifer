package jg.aquifer.ui.value;

import jg.aquifer.commands.options.Option;
import jg.aquifer.commands.options.VerificationException;

/**
 * Wraps the argument for an Option and it's verified status
 * 
 * An argument to an Option is verified (isVerified() returns true), if:
 * - The argument ISN'T null
 * - The argument has been successfully verified by the 
 *   Option's verifier (no VerificationException is thrown)
 * 
 * @author Jose
 */
public class ValueStatus {
  
  private final Option option;
  private final String value;
  private final VerificationException exception;

  /**
   * Constructs a Value that is initially verified
   * 
   * @param option - the Option this ValueStatus originates from
   * @param value - the String argument
   */
  public ValueStatus(Option option, String value) {
    this(option, value, null);
  }
  
  /**
   * Constructs a Value that is initially unverified
   * 
   * @param option - the Option this ValueStatus originates from
   * @param value - the String argument
   * @param exception - the VerificationException encountered while verifying the argument
   */
  public ValueStatus(Option option, String value, VerificationException exception) {
    this.option = option;
    this.value = value;
    this.exception = exception;
  }
  
  /**
   * Returns the String value
   * @return the String value
   */
  public String getValue() {
    return value;
  }
  
  /**
   * Returns the verification status of the backing String value
   * @return true if the associated Verifier with this Value's option
   *         has successfully verified the String value, false if else.
   */
  public boolean isVerified() {
    return exception == null && value != null;
  }

  public VerificationException getException() {
      return exception;
  }

  public Option getOption() {
      return option;
  }
  
  @Override
  public String toString() {
    return option+" = |"+value+"|  exception? "+exception;
  }
}
