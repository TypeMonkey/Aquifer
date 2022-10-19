package jg.aquifer.ui.io;

import java.io.InputStream;
import java.io.PrintWriter;

/**
 * Holds IO streams to print messages and receive input to/from the generated UI
 * 
 * @author Jose
 */
public class IO {
  
  public final PrintWriter out;
  public final PrintWriter err;
  public final InputStream in;
  
  public IO(PrintWriter standardOut, PrintWriter errorOut, InputStream in) {
    this.out = standardOut;
    this.err = errorOut;
    this.in = in;
  }
}
