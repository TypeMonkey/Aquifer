package jg.aquifer;

import java.util.Map;

import jg.aquifer.ui.io.IO;

/**
 * <p>
 * Represents the "drop-off" location for arguments submitted on the generated GUI, and the 
 * entry point for the actual backing application.
 * </p>
 * <p>
 * The generated GUI, after calling submitArguments(), will print out the contents
 * of the OutputStream - returned by getOutputStream() - on a separate pane. 
 * </p>
 * 
 * Note: {@link #submitArguments(String, Map, IO) submitArguments()} is called on the JavaFX thread. 
 *       This means that if the logic of your submitArguments() implementation is fairly heavy, it can "freeze" 
 *       the generated UI - notably the output page.
 *       
 *       If this is the case, consider offloading the logic of your submitArguments() implementation
 *       to a seperate thread, as such:
 * 
 *       <pre>
 *       {@code
 *       public void submitArguments(String subcommand, Map<String, String> arguments, IO streams) {
 *          //Offload method logic and call it in a seperate thread.
 *          Thread thread = new Thread(() -> {heavyLogic();});
 *          thread.start();
 *       }
 * 
 *       private void heavyLogic() {
 *           //Put "heavy" logic in this method.
 *       }}
 *       </pre>
 * 
 * @author Jose
 */
@FunctionalInterface
public interface Intake {

  /**
   * Submits the arguments provided on UI as a 
   * mapping of Options (as their name) to their provided Values (as a string).
   * 
   * Note: For selected Flags, they're included in the mapping, but their value is simply
   *       the string Flag.VALUE_PLACE_HOLDER.
   * 
   * @param subcommand - the subcommand being invoked
   * @param arguments - the arguments suppled on the UI
   * @param streams - a Stream object holding the various streams of output to the generated UI
   */
  public abstract void submitArguments(String subcommand, Map<String, String> arguments, IO streams);
  
}
