package jg.guifier.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jg.guifier.commands.Subcommand;
import jg.guifier.commands.options.Flag;
import jg.guifier.commands.options.Option;

/**
 * Houses the supplied arguments from the generated UI.
 * @author Jose Guaro
 */
public class RawArgumentForm {
  
  /**
   * A wrapper class for supplied arguments
   * @author Jose Guaro
   */
  public static class Value {
    private String value;
    private boolean badValue;
    
    public Value() {
      this.badValue = true;
    }
    
    public Value setValue(String value) {
      this.value = value;
      return this;
    }
    
    public String getValue() {
      return value;
    }
    
    public boolean isBadValue() {
      return badValue;
    }
    
    public Value validate() {
      this.badValue = false;
      return this;
    }
    
    public Value invalidate() {
      this.badValue = true;
      return this;
    }
    
    @Override
    public String toString() {
      return "|"+value+"|  isBadValue? "+badValue;
    }
  }
  
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
