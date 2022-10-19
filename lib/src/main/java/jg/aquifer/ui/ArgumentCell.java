package jg.aquifer.ui;

import javafx.scene.control.ListCell;
import jg.aquifer.commands.Subcommand;
import jg.aquifer.commands.options.Option;

/**
 * Custom display factory for the individual cells on a ListView.
 * 
 * This allows us to create custom graphics for each different
 * child of Option.
 * 
 * @author Jose
 */
public class ArgumentCell extends ListCell<Option> {
  
  private final Subcommand subcommand;
  private final RawArgumentForm argumentForm;
    
  public ArgumentCell(Subcommand subcommand, RawArgumentForm argumentForm) {
    // TODO Auto-generated constructor stub
    this.subcommand = subcommand;
    this.argumentForm = argumentForm;
  }

  @Override
  protected void updateItem(Option item, boolean empty) {
    // TODO Auto-generated method stub
    super.updateItem(item, empty);

    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    } else {     
      setGraphic(item.makeDisplay(argumentForm, subcommand));
    }
  }
}
