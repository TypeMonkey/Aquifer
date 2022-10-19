package jg.aquifer.commands.options;

/**
 * An exception thrown by an Option's Verifier
 * @author Jose
 */
public class VerificationException extends Exception {

  public VerificationException(String reason) {
    super(reason);
  }
  
}
