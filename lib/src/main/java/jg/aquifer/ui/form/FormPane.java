package jg.aquifer.ui.form;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.util.Callback;
import jg.aquifer.commands.Program;
import jg.aquifer.commands.Subcommand;
import jg.aquifer.commands.options.Flag;
import jg.aquifer.commands.options.Option;
import jg.aquifer.ui.ArgumentCell;
import jg.aquifer.ui.RawArgumentForm;

/**
 * The "main" page where users supply
 * program/subprogram arguments.
 */
public class FormPane extends Pane implements FormConstants {
  private static final Logger LOG = LoggerFactory.getLogger(FormPane.class);

  private final Program program;
  private final Pane headerPane;
  private final Runnable closeOperation;
  private final Consumer<RawArgumentForm> intake;
  private final Map<String, RawArgumentForm> rawArguments;

  private volatile RawArgumentForm currentForm;
    
  public FormPane(Pane headerPane, 
                  Program program, 
                  Runnable closeOperation,
                  Consumer<RawArgumentForm> intake) {
    this.headerPane = headerPane;
    this.program = program;
    this.closeOperation = closeOperation;
    this.intake = intake;
    this.rawArguments = new HashMap<>();    

    applyLayout();
  }

  private void applyLayout() {
    //We use a StackPane to hold everything at the center of our window
    final StackPane contentPane = new StackPane();
    contentPane.setAlignment(Pos.CENTER);
    contentPane.setId(CONTENT_PANE);

    //We'll be using a VBox to actually layout our components
    final VBox mainVBox = new VBox();
    mainVBox.setId(CONTENT_VBOX);

    /*
     * Our layout has three main parts:
     * - Header - Overall program description and icon
     * - Body - the place where the user enters arguments
     * - Footer - Has the "Run" and "Cancel" buttons
     */
    final TabPane formBody = createBody();
    final Pane footer = createFooter();
    VBox.setVgrow(formBody, Priority.ALWAYS);
    mainVBox.getChildren().addAll(headerPane, formBody, footer);

    contentPane.getChildren().add(mainVBox);

    contentPane.setPrefSize(PREF_WIDTH, PREF_HEIGHT);

    //Lastly, our contentPane will be the only thing we add to the parent
    getChildren().add(contentPane);
  }

  private TabPane createBody() {
    /*
     * Each Subprogram has it's own tab.
     * These tabs will be held by the below TabPane
     */
    final TabPane bodyPane = new TabPane();
    bodyPane.setId(TAB_PANE);
    
    /*
     * Add the Program's Subcommand representation first.
     * 
     * This is essential as we want this Subcommand to be
     * displayed first.
     */
    final Tab programTab = generateSubcommandPane(program.getSubcommandRepr());
    bodyPane.getTabs().add(programTab);
    bodyPane.requestLayout();

    for (Subcommand subcommand : program.getSubcommands().values()) {
      if (subcommand != program.getSubcommandRepr()) {
        final Tab tab = generateSubcommandPane(subcommand);
        bodyPane.getTabs().add(tab);
        bodyPane.requestLayout();
      }
    }
    
    bodyPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() { 
      @Override 
      public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
        currentForm = rawArguments.get(newTab.getText());
      }
    });
    
    /*
     * Set currentForm to be the corresponding RawArgumentForm of the currently selected Tab 
     */
    currentForm = rawArguments.get(bodyPane.getSelectionModel().selectedItemProperty().get().getText());
    
    return bodyPane;
  }

  private Tab generateSubcommandPane(Subcommand subcommand) {
    //Create ID for this tab
    final String TAB_ID = subcommand.getName()+TAB_SUFFIX;

    //Each subcommand (including the program itself) corresponds to a Tab
    final Tab tab = new Tab(subcommand.getName());   
    tab.setId(TAB_ID);

    /*
     * We'll use a VBox to lign up three Option categories:
     * 
     * Listed in order of appearance:
     * -> Required options
     * -> Options that are not Flags and not required
     * -> Flag
     */
    final VBox paneVBox = new VBox();
    paneVBox.setId(TAB_ID+TAB_CONTENT_SUFFIX);

    paneVBox.setPrefHeight(300);
    paneVBox.setAlignment(Pos.CENTER);
    
    /**
     * If this Subcommand has a non-empty description, we'll
     * put it in our VBox first above all the options
     */
    if (subcommand.getDescription() != null && !subcommand.getDescription().isEmpty()) {
      final Label subcommandDescLabel = new Label(subcommand.getDescription());
      subcommandDescLabel.setFont(Font.font("System Regular", FontPosture.ITALIC, 14));
      paneVBox.getChildren().add(subcommandDescLabel);
    }
    
    /*
     * Each Subcommand has a corresponding RawArgumentForm. 
     */
    final RawArgumentForm subcommandForm = new RawArgumentForm(subcommand);
    rawArguments.put(subcommand.getName(), subcommandForm);
    
    /*
     * Recall that we categorize a Subcommand's options in the following
     * groups in order of appearance:
     * 
     * 1. All required options
     * 2. All non-Flag, unrequired options
     * 3. All options that are Flags
     * 
     * These three categories correspond to a ListView<Option> 
     * and TitledPane to hold that ListView
     */

    //Required options ListView
    final ListView<Option> requiredArgsFlow = new ListView<>();
    {
      requiredArgsFlow.setId(TAB_ID+TAB_REQ_LIST_SUFFIX);

      requiredArgsFlow.setCellFactory(new Callback<ListView<Option>, ListCell<Option>>() {  
        @Override
        public ListCell<Option> call(ListView<Option> param) {
          LOG.info("---returning new view");
          return new ArgumentCell(subcommand, subcommandForm);
        }
      });
      
      //Required options TitledPane
      final TitledPane requiredArgs = new TitledPane("Required Arguments", requiredArgsFlow);
      requiredArgs.setId(TAB_ID+TAB_REQ_PANE_SUFFIX);

      paneVBox.getChildren().add(requiredArgs);
      requiredArgs.setCollapsible(false);
      VBox.setVgrow(requiredArgs, Priority.ALWAYS);
    }

    //Optional options ListView
    final ListView<Option> optionalArgsFlow = new ListView<>();
    {
      optionalArgsFlow.setCellFactory(new Callback<ListView<Option>, ListCell<Option>>() {  
        @Override
        public ListCell<Option> call(ListView<Option> param) {
          LOG.info("---returning new view");
          return new ArgumentCell(subcommand, subcommandForm);
        }
      });
      
      //Optional options TitledPane
      final TitledPane optionalArgs = new TitledPane("Optional Arguments", optionalArgsFlow);
      optionalArgs.setId(TAB_ID+TAB_REQ_PANE_SUFFIX);

      paneVBox.getChildren().add(optionalArgs);
      optionalArgs.setCollapsible(false);
      VBox.setVgrow(optionalArgs, Priority.ALWAYS);
    }

    //Optional options ListView
    final ListView<Option> flagsFlow = new ListView<>();
    {
      flagsFlow.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      flagsFlow.setCellFactory(new Callback<ListView<Option>, ListCell<Option>>() {  
        @Override
        public ListCell<Option> call(ListView<Option> param) {
          LOG.info("---returning new view");
          return new ArgumentCell(subcommand, subcommandForm);
        }
      });
      
      //Optional options TitledPane
      final TitledPane flagsArgs = new TitledPane("Flags", flagsFlow);
      flagsArgs.setId(TAB_ID+TAB_FLAG_PANE_SUFFIX);

      paneVBox.getChildren().add(flagsArgs);
      flagsArgs.setCollapsible(false);
      VBox.setVgrow(flagsArgs, Priority.ALWAYS);
    }
    
    for(Option option : subcommand.getOptions().values()) {      
      LOG.info(option.getOptName()+" | "+option.isRequired());
      
      subcommandForm.setOptionArgument(option, option.getHolder());
      
      if (option instanceof Flag) {
        flagsFlow.getItems().add(option);
      }       
      else if (option.isRequired()) {
        requiredArgsFlow.getItems().add(option);
      }
      else {
        optionalArgsFlow.getItems().add(option);
      }
    }
    
    AnchorPane.setBottomAnchor(paneVBox, 0.0);
    AnchorPane.setTopAnchor(paneVBox, 0.0);
    AnchorPane.setLeftAnchor(paneVBox, 0.0);
    AnchorPane.setRightAnchor(paneVBox, 0.0);    
    
    //We place our VBox in an AnchorPane so it resizes with the rest of the UI
    AnchorPane anchoredPane = new AnchorPane(paneVBox);
    anchoredPane.setId(TAB_ID+TAB_ANCHOR_SUFFIX);

    //anchoredPane.prefHeight(100);
    //anchoredPane.prefHeightProperty().bind(bodyPane.prefHeightProperty());

    //Finally, we set our anchoredPane as our actual contentPane
    tab.setContent(anchoredPane);
    return tab;
  }

  private Pane createFooter() {
    /*
     * Our footer contains only two buttons, "Run" and "Cancel".
     * We'll use an HBox to align these buttons
     */
    final HBox footerBox = new HBox(10);
    footerBox.setId(FOOTER);
    footerBox.setAlignment(Pos.CENTER);
    
    final Button runButton = new Button("Run");
    runButton.setId(RUN_BUTTON);
    runButton.setPrefWidth(60);

    HBox.setMargin(runButton, new Insets(10));
    HBox.setHgrow(runButton, Priority.ALWAYS);
    runButton.setOnAction((event) -> {
      LOG.info("current intake: "+currentForm.getRawArguments().keySet());
      LOG.info(rawArguments.toString());

      intake.accept(currentForm);
    });
       
    final Button cancelButton = new Button("Cancel");
    cancelButton.setId(CANCEL_BUTTON);
    cancelButton.setPrefWidth(60);

    HBox.setMargin(cancelButton, new Insets(10));
    HBox.setHgrow(cancelButton, Priority.ALWAYS);
    cancelButton.setOnAction((event) -> closeOperation.run());
   
    footerBox.getChildren().addAll(runButton, cancelButton);
    
    return footerBox;
  }
 
  public RawArgumentForm getCurrentForm() {
    return currentForm;
  }
}
