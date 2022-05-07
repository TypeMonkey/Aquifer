package jg.aquifer;

import java.io.OutputStream;
import java.util.Map;

import jg.aquifer.commands.Subcommand;

/**
 * Represents the "drop-off" location for arguments submitted on the generated GUI, and the 
 * entry point for the actual backing application.
 * 
 * The generated GUI, after calling submitArguments(), will print out the contents
 * of the OutputStream - returned by getOutputStream() - on a separate pane. Developers are more than 
 * welcome to create a separate OutputStream for the GUI to listen on.
 * 
 * Note: It's highly suggested that the backing application be called in a separate Thread as to not block
 * the generated UI.
 * 
 * @author Jose
 */
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
   * @param printer - the function to call to print output messages to the UI
   */
  public abstract void submitArguments(String subcommand, Map<String, String> arguments, Printer printer);
  
}
