package jg.aquifer.commands.options;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
import jg.aquifer.commands.Verifier;
import jg.aquifer.ui.RawArgumentForm;
import jg.aquifer.ui.value.ExclusiveValueStatus;
import jg.aquifer.ui.value.ValueStatus;


public class ExclusiveOptions extends Option {

  private static final Logger LOG = LoggerFactory.getLogger(ExclusiveOptions.class);

  private final List<Option> options;

  public ExclusiveOptions(String optName, 
                          String description, 
                          List<Option> options,
                          boolean isRequired) {
    super(optName, description, isRequired, makeExclusiveVerifier(options));
    this.options = Collections.unmodifiableList(options);
  }

  private static Verifier makeExclusiveVerifier(List<Option> options) {
    return (op, form, arg) -> {
      List<Option> givenOptions = new ArrayList<>();

      for (Option option : options) {
        if (option.valueProperty.getValue() != null && 
            option.valueProperty.getValue().isVerified()) {
          givenOptions.add(option);
        }
      }

      if (givenOptions.size() > 1) {
        String mess = "Only one option allowed to be selected. "+System.lineSeparator();
        mess       += "The options '"+givenOptions.stream().map(x -> x.getOptName()).collect(Collectors.joining(","))+
                      " have all been selected";
        throw new VerificationException(mess);
      }
    };
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

    final Label exceptionLabel = new Label();
    exceptionLabel.setTextFill(Color.RED);

    final ChangeListener<ValueStatus> changeListener = (o, oldValue, newValue) -> {
      try {
        getVerifier().verify(this, argumentForm, getDescription());
        valueProperty.set(new ExclusiveValueStatus(this, newValue, newValue.getException()));
        mainCellLayout.getChildren().remove(exceptionLabel);
      } catch (VerificationException e) {
        if(!mainCellLayout.getChildren().contains(exceptionLabel)) {
          exceptionLabel.setText(e.getMessage());
          mainCellLayout.getChildren().add(exceptionLabel);
        }
        
        valueProperty.set(new ExclusiveValueStatus(this, newValue, e));
      }     
    };

    //DEV NOTE: Remove this. Purely for testing
    valueProperty.addListener((o, oldV, newV) -> {
      LOG.info("Exclusive option given: "+newV);
    });

    for (Option option : options) {
      final Pane optionPane = option.makeDisplay(argumentForm, subcommand);
      optionPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
      optionPane.setPadding(new Insets(5,5,5,5));
      optionPane.setBorder(new Border(new BorderStroke(Color.DARKGREY, 
                                                 BorderStrokeStyle.SOLID, 
                                                 new CornerRadii(.020, true), 
                                                 new BorderWidths(1))));

      optionsFlowPane.getChildren().add(optionPane);
      option.valueProperty().addListener(changeListener);
    }

    mainCellLayout.getChildren().add(optionsFlowPane);    
    return mainCellLayout;
  }
  
  public List<Option> getOptions() {
    return options;
  }
}
