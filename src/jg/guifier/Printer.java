package jg.guifier;

/**
 * An interface that allows piping of string output
 * to the generated UI
 * @author Jose Guaro
 *
 */
public interface Printer {

  /**
   * Prints the given message on a new line
   * @param content - the content to print 
   */
  public void println(String content);
  
}
