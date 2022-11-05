package jg.aquifer.commands.options;

import java.util.Collections;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
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


public class ExclusiveOptions extends Option {

  private final List<Option> options;

  public ExclusiveOptions(String optName, 
                          String description, 
                          List<Option> options,
                          boolean isRequired) {
    super(optName, description, isRequired);
    this.options = Collections.unmodifiableList(options);
  }

  @Override
  protected Pane generateDisplay(RawArgumentForm argumentForm, Subcommand subcommand) {
    final VBox mainCellLayout = new VBox(5);

    final Text argumentName = new Text(getOptName());
    argumentName.setFont(Font.font("System Regular", FontWeight.BOLD, 14));
    
    final Text argumentDescription = new Text(getDescription());
    argumentDescription.setFont(Font.font("System Regular", FontPosture.ITALIC, 14));
    
    final TextFlow flow = new TextFlow(argumentName, new Text(System.lineSeparator()), argumentDescription);
    mainCellLayout.getChildren().add(flow);

    final FlowPane optionsFlowPane = new FlowPane(Orientation.HORIZONTAL);
    optionsFlowPane.setBorder(Border.EMPTY);
    optionsFlowPane.setHgap(5);
    optionsFlowPane.setVgap(5);
    optionsFlowPane.setPadding(new Insets(5,5,0,0));
    optionsFlowPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

    for (Option option : options) {
      final Pane optionPane = option.makeDisplay(argumentForm, subcommand);
      optionPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
      optionPane.setPadding(new Insets(5,5,5,5));
      optionPane.setBorder(new Border(new BorderStroke(Color.DARKGREY, 
                                                 BorderStrokeStyle.SOLID, 
                                                 new CornerRadii(.020, true), 
                                                 new BorderWidths(1))));

      optionsFlowPane.getChildren().add(optionPane);
    }

    mainCellLayout.getChildren().add(optionsFlowPane);    
    return mainCellLayout;
  }
  
  public List<Option> getOptions() {
      return options;
  }
}
