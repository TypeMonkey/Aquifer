package jg.guifier.commands.options;

/**
 * An exception thrown when an argument to an option 
 * doesn't match that option's requirements
 * 
 * @author Jose
 */
public class VerificationException extends Exception{

  public VerificationException(String reason) {
    super(reason);
  }
  
}
