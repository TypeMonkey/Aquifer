package jg.aquifer;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * Holds IO streams to print strings to the generated UI
 * 
 * @author Jose
 */
public class Output {
  
  public final PrintWriter out;
  public final PrintWriter err;
  
  public Output(PrintWriter standardOut, PrintWriter errorOut) {
    this.out = standardOut;
    this.err = errorOut;
  }

  public PrintWriter getStdOut() {
    return out;
  }
  
  public PrintWriter getStdErr() {
    return err;
  }
}
