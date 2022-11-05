package jg.aquifer.commands.options;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jg.aquifer.commands.Subcommand;
import jg.aquifer.ui.RawArgumentForm;

/**
 * A RadioOption is restricted to a collection of possible choices, 
 * and only one choice must be made.
 * 
 * This is synonymous to radio buttons in common UI toolkits, where
 * users are only allowed to select one button in a group of buttons.
 * 
 * @author Jose
 *
 */
public class RadioOption extends Option {
  
  private final String [] choices;
  
  /**
   * Constructs a RadioOption
   * @param optName - the name of this RadioOption
   * @param description - the description of this RadioOption
   * @param isRequired - whether this RadioOption is required
   * @param choices - the possible choices, as Strings, for this RadioOption
   */
  public RadioOption(String optName, String description, boolean isRequired, String ... choices) {
    super(optName, description, isRequired);
    this.choices = choices;
  }
  
  @Override
  public Pane generateDisplay(RawArgumentForm argumentForm, Subcommand subcommand) {
    final VBox mainCellLayout = new VBox();
    
    final Text argumentName = new Text(getOptName());
    argumentName.setFont(Font.font("System Regular", FontWeight.BOLD, 14));
    
    final Text argumentDescription = new Text(getDescription());
    argumentDescription.setFont(Font.font("System Regular", FontPosture.ITALIC, 14));
    
    final TextFlow flow = new TextFlow(argumentName, new Text(System.lineSeparator()), argumentDescription);
    flow.setPadding(new Insets(0, 0, 5, 0));
    mainCellLayout.getChildren().add(flow);
    
    final HBox entryCellHBox = new HBox(5);
    
    /*
    final Label exceptionLabel = new Label();
    exceptionLabel.setTextFill(Color.RED);
    */
    
    final ToggleGroup selectionGroup = new ToggleGroup();
    for (String string : choices) {
      RadioButton radioButton = new RadioButton(string);
      radioButton.setToggleGroup(selectionGroup);      
      entryCellHBox.getChildren().add(radioButton);
    }
    
    final Option currentOption = this;
    
    selectionGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
      
      @Override
      public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        RadioButton selectedButton = (RadioButton) newValue;
        final String selectedValue = selectedButton.getText();
        
        try {
          getVerifier().verify(currentOption, argumentForm, selectedValue);
          setValue(selectedValue);
        } catch (VerificationException e) {
          //This should never be thrown as the Verifier of a RadioOption is Verifier.STR_VERIFIER
        }   
      }
    });    
    
    mainCellLayout.getChildren().add(entryCellHBox);
    
    return mainCellLayout;
  }

  public String[] getChoices() {
    return choices;
  }
}
