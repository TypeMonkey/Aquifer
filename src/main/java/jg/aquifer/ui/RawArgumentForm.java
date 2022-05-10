package jg.aquifer.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jg.aquifer.commands.Subcommand;
import jg.aquifer.commands.options.Flag;
import jg.aquifer.commands.options.Option;

/**
 * Houses the supplied arguments from the generated UI.
 * @author Jose Guaro
 */
public class RawArgumentForm {
  
  private Subcommand subcommand;
  private Map<Option, Value> rawArguments;

  public RawArgumentForm(Subcommand subcommand) {
    this.subcommand = subcommand;
    this.rawArguments = new HashMap<>();
  }
  
  public void setOptionArgument(Option option, Value newValue) {
    rawArguments.put(option, newValue);
  }
  
  public Value getOptionArgument(Option option) {
    return rawArguments.get(option);
  }
  
  public boolean hasOption(Option option) {
    return rawArguments.containsKey(option);
  }
  
  public Map<Option, Value> getSubcommandOptions(Subcommand subcommand){
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
