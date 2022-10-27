package jg.aquifer.ui;

import java.util.HashMap;
import java.util.Map;

import jg.aquifer.commands.Subcommand;
import jg.aquifer.commands.options.Option;

/**
 * Houses the supplied arguments to a Subcommand.
 * @author Jose Guaro
 */
public class RawArgumentForm {
  
  private Subcommand subcommand;
  private Map<Option, Value> rawArguments;

  /**
   * Constructs an empty RawArgumentForm
   * @param subcommand - the Subcommand this form is meant for
   */
  public RawArgumentForm(Subcommand subcommand) {
    this.subcommand = subcommand;
    this.rawArguments = new HashMap<>();
  }
  
  /**
   * Sets the Value of an Option
   * @param option - an Option
   * @param newValue - a Value
   */
  public void setOptionArgument(Option option, Value newValue) {
    rawArguments.put(option, newValue);
  }
  
  /**
   * Retrieves the associated Value of the given Option
   * @param option - the Option whose Value is desired
   * @return the associated Value, 
   *         or null if the given Option hasn't been set on this RawArgumentForm
   */
  public Value getOptionArgument(Option option) {
    return rawArguments.get(option);
  }
  
  /**
   * Whether this RawArgumentForm has the given Option
   * @param option - the Option to check for
   * @return true if this Option is contained in this RawArgumentForm, 
   *         false if else
   */
  public boolean hasOption(Option option) {
    return rawArguments.containsKey(option);
  }
  
  public Map<Option, Value> getSubcommandOptions(){
    return rawArguments;
  }
  
  public Map<Option, Value> getRawArguments() {
    return rawArguments;
  }
  
  public Subcommand getSubcommand() {
    return subcommand;
  }
  
  @Override
  public String toString() {
    return "Subcommand: "+subcommand.getName()+" | "+rawArguments.toString();
  }
}