package jg.aquifer.commands.options;

import java.util.Collections;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import jg.aquifer.commands.Subcommand;
import jg.aquifer.ui.RawArgumentForm;
import jg.aquifer.ui.ArgumentCell;


public class ExclusiveOptions extends Option {

  private final Set<Option> options;

  public ExclusiveOptions(String optName, 
                          String description, 
                          Set<Option> options,
                          boolean isRequired) {
    super(optName, description, isRequired);
    this.options = Collections.unmodifiableSet(options);
  }

  @Override
  protected Node generateDisplay(RawArgumentForm argumentForm, Subcommand subcommand) {
    final VBox mainCellLayout = new VBox(5);

    final Text argumentName = new Text(getOptName());
    argumentName.setFont(Font.font("System Regular", FontWeight.BOLD, 14));
    
    final Text argumentDescription = new Text(getDescription());
    argumentDescription.setFont(Font.font("System Regular", FontPosture.ITALIC, 14));
    
    final TextFlow flow = new TextFlow(argumentName, new Text(System.lineSeparator()), argumentDescription);
    mainCellLayout.getChildren().add(flow);

    final ListView<Option> listedOptions = new ListView<>(FXCollections.observableArrayList(options));
    listedOptions.setCellFactory(new Callback<ListView<Option>, ListCell<Option>>() {  
      @Override
      public ListCell<Option> call(ListView<Option> param) {
        return new ArgumentCell(subcommand, argumentForm);
      }
    });

    mainCellLayout.getChildren().add(listedOptions);
    
    return mainCellLayout;
  }
  
  public Set<Option> getOptions() {
      return options;
  }
}
