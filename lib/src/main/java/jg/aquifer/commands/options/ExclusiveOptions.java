package jg.aquifer.commands.options;

import java.util.Set;

import javafx.scene.Node;
import jg.aquifer.commands.Subcommand;
import jg.aquifer.ui.RawArgumentForm;

public class ExclusiveOptions extends Option {

  public ExclusiveOptions(String optName, 
                          String description, 
                          Set<Option> options,
                          boolean isRequired) {
    super(optName, description, isRequired);
  }

  @Override
  protected Node generateDisplay(RawArgumentForm argumentForm, Subcommand subcommand) {
    // TODO Auto-generated method stub
    return super.generateDisplay(argumentForm, subcommand);
  }
  
}
