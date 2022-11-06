package jg.aquifer.commands.options;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jg.aquifer.commands.Subcommand;
import jg.aquifer.ui.RawArgumentForm;
import jg.aquifer.ui.value.ValueStatus;

/**
 * A Flag is a non-required Option that has no arguments.
 * @author Jose
 */
public class Flag extends Option {
  
  /**
   * The string used to signify that a Flag has been selected
   */
  public static final String FLAG_SELECTED_HOLDER = "selected";

  /**
   * The string used to signify that a Flag has been selected
   */
  public static final String FLAG_UNSELECTED_HOLDER = "unselected";

  /**
   * Constructs an optional Option with a passive Verifier (Verifier.STR_VERIFIER)
   * @param optName - the name of this Option
   * @param description - the description of this Option
   */
  public Flag(String optName, String description) throws IllegalArgumentException {
    super(optName, description, false);
  }

  @Override
  public Pane generateDisplay(RawArgumentForm argumentForm, Subcommand subcommand) {
    final HBox mainLayout = new HBox();
    mainLayout.setAlignment(Pos.CENTER_LEFT);
        
    final CheckBox checkBox = new CheckBox();
    checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue == Boolean.TRUE) {
          processArgument(FLAG_SELECTED_HOLDER, argumentForm, subcommand, EMPTY_RUNNABLE, EMPTY_EXC_CON);
        }
        else {
          processArgument(FLAG_UNSELECTED_HOLDER, argumentForm, subcommand, EMPTY_RUNNABLE, EMPTY_EXC_CON);
        }
    });

    mainLayout.getChildren().add(checkBox);
    
    final Text argumentName = new Text(getOptName());
    argumentName.setFont(Font.font("System Regular", FontWeight.BOLD, 14));
    
    final Text argumentDescription = new Text(getDescription());
    argumentDescription.setFont(Font.font("System Regular", FontPosture.ITALIC, 14));
    
    final TextFlow flow = new TextFlow(argumentName, new Text(System.lineSeparator()), argumentDescription);
    mainLayout.getChildren().add(flow);
    
    return mainLayout;
  }

}
