package jg.guifier.ui;

import java.io.PipedInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Callback;
import jg.guifier.Intake;
import jg.guifier.commands.Program;
import jg.guifier.commands.Subcommand;
import jg.guifier.commands.options.Flag;
import jg.guifier.commands.options.Option;
import jg.guifier.ui.RawArgumentForm.Value;

public class Visualizer {
  
  private Program program;
  private Intake intake;
  
  private final Stage content;
  private Scene optionsScene;
  private Scene outputScene;
  private Callback<String, Void> outputAppendFunc;
      
  private Map<String, RawArgumentForm> rawArguments;
  private volatile RawArgumentForm currentForm;
  
  private volatile boolean isShowing;
  private volatile boolean isInitialized;
  
  public Visualizer(Program program, Intake intake) {
    this.program = program;
    this.intake = intake;
    this.rawArguments = new HashMap<>();
    this.content = new Stage();
  }
  
  /**
   * Generates and prepares the UI for use. 
   * Must be called prior to show().
   */
  public void initialize() {
    if (!isInitialized) {
      Pane contentPane = generate();
      optionsScene = new Scene(contentPane);
      
      Pane outputPane = generateOutputScene();
      outputScene = new Scene(outputPane);    
      
      isInitialized = true;
    }
  }
  
  /**
   * Displays the generated UI. 
   * 
   * Note: This method does not block.
   * @throws IllegalStateException - if this Visualizer hasn't been initialized, 
   *                                 or the UI is already being displayed.
   */
  public void show() throws IllegalStateException {
    if (!isInitialized) {
      throw new IllegalStateException("Visualizer has not been initialized!");
    }
    else if (isShowing) {
      throw new IllegalStateException("UI is already displaying!");
    }
    
    content.setScene(optionsScene);
    content.show();
    content.centerOnScreen();
    
    isShowing = true;
  }
  
  /**
   * Generates the UI representing the Program's options
   * @return the Pane housing the generated graphic
   */
  private Pane generate() { 
    final StackPane contentPane = new StackPane();
    
    final VBox mainContentVBox = new VBox();
    
    final Pane header = generateHeader();
    final TabPane body = generateBody();
    final Pane footer = generateFooter();
    
    VBox.setVgrow(body, Priority.ALWAYS);
        
    mainContentVBox.getChildren().addAll(header, body, footer);
        
    contentPane.getChildren().add(mainContentVBox);
    
    return contentPane;
  }
  
  private void callIntake() {
    System.out.println("current intake: "+currentForm.getRawArguments().keySet());
    System.out.println(rawArguments);
        
    try {
      Map<String, String> processedForm = processArgs(currentForm);
          
      intake.submitArguments(currentForm.getSubcommand().getName(), 
                             processedForm, 
                             (content) -> {
                               Platform.runLater(() -> {outputAppendFunc.call(content);});
                             });
      content.setScene(outputScene);
      content.centerOnScreen();
    } catch (IncompleteException e) {
      Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
      alert.setTitle("Missing Required Options");
      alert.setResizable(false);
      alert.showAndWait();
    }
  }
  
  private Pane generateOutputScene() {   
    final VBox mainContentVBox = new VBox();
    final Pane header = generateHeader();
    mainContentVBox.getChildren().add(header);
    
    final AnchorPane bodyPane = new AnchorPane();
    final TextArea outputArea = new TextArea("***Application Output:***"+System.lineSeparator());
    outputArea.setWrapText(true);
    outputArea.setEditable(false);
    
    this.outputAppendFunc = new Callback<String, Void>() {
      @Override
      public Void call(String param) {
        outputArea.appendText(param+System.lineSeparator());
        return null;
      }
    };
    
    final ScrollPane scrollPane = new ScrollPane(outputArea);
    scrollPane.setFitToHeight(true);
    scrollPane.setFitToWidth(true);
    
    AnchorPane.setBottomAnchor(outputArea, 0.0);
    AnchorPane.setTopAnchor(outputArea, 0.0);
    AnchorPane.setLeftAnchor(outputArea, 0.0);
    AnchorPane.setRightAnchor(outputArea, 0.0);
    
    AnchorPane.setBottomAnchor(scrollPane, 0.0);
    AnchorPane.setTopAnchor(scrollPane, 0.0);
    AnchorPane.setLeftAnchor(scrollPane, 0.0);
    AnchorPane.setRightAnchor(scrollPane, 0.0);
    
    VBox.setVgrow(bodyPane, Priority.ALWAYS);
    
    scrollPane.setPadding(new Insets(10));
    bodyPane.getChildren().add(scrollPane);
                
    mainContentVBox.getChildren().add(bodyPane);
    
    final Button cancelButton = new Button("Cancel");
    cancelButton.setOnAction((event) -> {
      Alert alert = new Alert(AlertType.CONFIRMATION, 
                              "Are you sure you want to cancel?", 
                              ButtonType.YES, ButtonType.NO);
      alert.setTitle("Cancel");
      alert.setResizable(false);
      alert.showAndWait();
      
      if (alert.getResult() == ButtonType.YES) {
        Platform.exit();
        System.exit(1);
      }
    });
    cancelButton.setPrefWidth(60);
    HBox.setMargin(cancelButton, new Insets(10));
    HBox.setHgrow(cancelButton, Priority.ALWAYS);
    
    final HBox footer = new HBox(cancelButton);
    footer.setAlignment(Pos.CENTER);
    
    mainContentVBox.getChildren().add(footer);
            
    return mainContentVBox;
  }
  
  private Map<String, String> processArgs(RawArgumentForm formToProcess) throws IncompleteException {
    final Map<String, String> args = new HashMap<>();
    final Set<String> givenRequiredOptions = new HashSet<>();
    
    for (Entry<Option, Value> argVal: formToProcess.getRawArguments().entrySet()) {
      
      System.out.println("--- processing: "+argVal.getKey()+" <-> "+argVal.getValue());
      
      if (argVal.getValue().getValue() != null && !argVal.getValue().getValue().isEmpty() && !argVal.getValue().isBadValue()) {
        args.put(argVal.getKey().getOptName(), argVal.getValue().getValue());
        
        System.out.println("    -> valid :D");
        
        if (argVal.getKey().isRequired()) {
          givenRequiredOptions.add(argVal.getKey().getOptName());
        }
      }
    }
    
    final Set<String> requiredOptions = new HashSet<>(formToProcess.getSubcommand().getRequiredOptions().keySet());
    System.out.println("---  given required options "+givenRequiredOptions);
    if (!givenRequiredOptions.equals(requiredOptions)) {
      
      System.out.println(givenRequiredOptions);
      
      requiredOptions.removeAll(givenRequiredOptions);
      throw new IncompleteException(formToProcess.getSubcommand(), requiredOptions);
    }
    
    return args;
  }
  
  private Pane generateHeader() {
    
    final HBox header = new HBox(10);
    
    System.out.println("---header");
    System.out.println(header.getStyle()+"||");
    
    header.setStyle("-fx-background-color: white");
    header.setAlignment(Pos.CENTER);
    
    final Text programTitle = new Text(program.getName());
    programTitle.setFont(Font.font("System Regular", FontWeight.BOLD, 14));
    final Text programDesc = new Text(program.getDescription());
    programDesc.setFont(Font.font("System Regular", FontPosture.ITALIC, 14));
    final TextFlow descriptionFlow = new TextFlow(programTitle, new Text(System.lineSeparator()), programDesc);
    descriptionFlow.setPadding(new Insets(10, 10, 10, 10));
    
    HBox.setHgrow(descriptionFlow, Priority.ALWAYS);
    
    header.getChildren().add(descriptionFlow);
    
    if(program.getImage() != null) {
      final ImageView programIcon = new ImageView(program.getImage());
      programIcon.setPreserveRatio(true);
      programIcon.setFitHeight(40);
      programIcon.setFitWidth(40);
      
      final StackPane iconPane = new StackPane(programIcon);
      iconPane.setAlignment(Pos.CENTER);
      iconPane.setPadding(new Insets(10, 10, 10, 10));
      
      HBox.setHgrow(iconPane, Priority.NEVER);
      header.getChildren().add(iconPane);
    }
    
    return header;
  }
  
  private TabPane generateBody() {
    final TabPane bodyPane = new TabPane();
    
    //Add the Program's Subcommand instance first
    final Tab programTab = generateSubcommandPane(program.getProgramOptions());
    bodyPane.getTabs().add(programTab);
    bodyPane.requestLayout();

    for (Subcommand subcommand : program.getSubcommands().values()) {
      if (subcommand != program.getProgramOptions()) {
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
    final Tab tab = new Tab(subcommand.getName());   
    final VBox paneVBox = new VBox();
    paneVBox.setPrefHeight(300);
    paneVBox.setAlignment(Pos.CENTER);
    
    if (subcommand.getDescription() != null && !subcommand.getDescription().isEmpty()) {
      final Label subcommandDescLabel = new Label(subcommand.getDescription());
      subcommandDescLabel.setFont(Font.font("System Regular", FontPosture.ITALIC, 14));
      paneVBox.getChildren().add(subcommandDescLabel);
    }
    
    final RawArgumentForm subcommandForm = new RawArgumentForm(subcommand);
    rawArguments.put(subcommand.getName(), subcommandForm);
    
    final ListView<Option> requiredArgsFlow = new ListView<>();
    requiredArgsFlow.setCellFactory(new Callback<ListView<Option>, ListCell<Option>>() {  
      @Override
      public ListCell<Option> call(ListView<Option> param) {
        System.out.println("---returning new view");
        return new ArgumentCell(subcommand, subcommandForm);
      }
    });
    
    final TitledPane requiredArgs = new TitledPane("Required Arguments", requiredArgsFlow);
    paneVBox.getChildren().add(requiredArgs);
    requiredArgs.setCollapsible(false);
    VBox.setVgrow(requiredArgs, Priority.ALWAYS);
    
    final ListView<Option> optionalArgsFlow = new ListView<>();
    optionalArgsFlow.setCellFactory(new Callback<ListView<Option>, ListCell<Option>>() {  
      @Override
      public ListCell<Option> call(ListView<Option> param) {
        System.out.println("---returning new view");
        return new ArgumentCell(subcommand, subcommandForm);
      }
    });
    
    final TitledPane optionalArgs = new TitledPane("Optional Arguments", optionalArgsFlow);
    paneVBox.getChildren().add(optionalArgs);
    optionalArgs.setCollapsible(false);
    VBox.setVgrow(optionalArgs, Priority.ALWAYS);
    
    final ListView<Option> flagsFlow = new ListView<>();
    flagsFlow.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    flagsFlow.setCellFactory(new Callback<ListView<Option>, ListCell<Option>>() {  
      @Override
      public ListCell<Option> call(ListView<Option> param) {
        System.out.println("---returning new view");
        return new ArgumentCell(subcommand, subcommandForm);
      }
    });
    
    final TitledPane flagsArgs = new TitledPane("Flags", flagsFlow);
    paneVBox.getChildren().add(flagsArgs);
    flagsArgs.setCollapsible(false);
    VBox.setVgrow(flagsArgs, Priority.ALWAYS);
    
    
    for(Option option : subcommand.getOptions().values()) {      
      System.out.println(option.getOptName()+" | "+option.isRequired());
      
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
    
    AnchorPane anchoredPane = new AnchorPane(paneVBox);
    //anchoredPane.prefHeight(100);
    
    //anchoredPane.prefHeightProperty().bind(bodyPane.prefHeightProperty());

    
    tab.setContent(anchoredPane);
    return tab;
  }
  
  private Pane generateFooter() {
    final HBox footerBox = new HBox(10);
    footerBox.setAlignment(Pos.CENTER);
    
    final Button runButton = new Button("Run");
    runButton.setPrefWidth(60);
    HBox.setMargin(runButton, new Insets(10));
    HBox.setHgrow(runButton, Priority.ALWAYS);
    runButton.setOnAction((event) -> {
      callIntake();
    });
       
    final Button cancelButton = new Button("Cancel");
    cancelButton.setPrefWidth(60);
    HBox.setMargin(cancelButton, new Insets(10));
    HBox.setHgrow(cancelButton, Priority.ALWAYS);
    cancelButton.setOnAction((event) -> {Platform.exit();});
   
    footerBox.getChildren().addAll(runButton, cancelButton);
    
    return footerBox;
  }
  
  /**
   * Effectively hides the UI.
   * 
   * To fully stop the UI, consider calling javafx.application.Platform.exit().
   * Note though that if you're using JavaFX components anywhere else in your program,
   * they will be affected.
   */
  public void close() {
    content.close();
    isShowing = false;
  }
  
  public boolean isShowing() {
    return isShowing;
  }
  
  public boolean isInitialized() {
    return content != null;
  }
}
