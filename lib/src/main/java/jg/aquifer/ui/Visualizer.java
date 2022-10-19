package jg.aquifer.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import jg.aquifer.Intake;
import jg.aquifer.commands.Program;
import jg.aquifer.commands.options.Option;
import jg.aquifer.ui.form.FormConstants;
import jg.aquifer.ui.form.FormPane;
import jg.aquifer.ui.io.IO;
import jg.aquifer.ui.output.OutputPane;

/**
 * Houses logic for generating a JavaFX-based UI from a Program instance.
 * 
 * @author Jose
 */
public class Visualizer {

  private static final Logger LOG = LoggerFactory.getLogger(Visualizer.class);
  
  private Program program;
  private Intake intake;
  
  private final Stage content;
  private Scene optionsScene;
  private Scene outputScene;
  private IO io;
  
  private volatile boolean isShowing;
  private volatile boolean isInitialized;
  
  /**
   * Constructs a Visualizer
   * @param program - the Program to create a GUI for
   * @param intake - the Intake to submit arguments to
   */
  public Visualizer(Program program, Intake intake) {
    this.program = program;
    this.intake = intake;
    this.content = new Stage();
  }

  /**
   * Generates and prepares the UI for use. 
   * Must be called prior to show().
   * 
   * @throws IOException - thrown when UI I/O streams encounter an IOException
   */
  public void initialize() throws IOException {
    initialize(null);
  }
  
  /**
   * Generates and prepares the UI for use. 
   * Must be called prior to show().
   * 
   * @param editor - an Editor that's invoked after creating this UI's
   *                 FormPane and OutputPane
   * 
   * @throws IOException - thrown when UI I/O streams encounter an IOException
   */
  public void initialize(Editor editor) throws IOException {
    if (!isInitialized) {
      final Pane header = generateHeader();

      FormPane contentPane = new FormPane(generateHeader(),
                                      program, 
                                      () -> close(), 
                                      (f) -> callIntake(f));
      optionsScene = new Scene(contentPane);
      
      OutputPane outputPane = new OutputPane(header, () -> close());
      outputScene = new Scene(outputPane);   

      io = outputPane.getStreams();

      /**
       * If provided an Editor, call it and pass 
       * the created FormPane and OutputPane
       */
      if (editor != null) {
        editor.edit(contentPane, outputPane);
      }
            
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
   * Will verify a RawArgumentForm and report
   * missing/invalid arguments if any. If none are found,
   * it will invoke the Intake instance of this Visualizer
   * @param form - the RawArgumentForm to process
   */
  private void callIntake(RawArgumentForm form) {  
    try {
      Map<String, String> processedForm = processArgs(form);
          
      intake.submitArguments(form.getSubcommand().getName(), 
                             processedForm, 
                             io);
      content.setScene(outputScene);
      content.centerOnScreen();
    } catch (IncompleteException e) {
      Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
      alert.setTitle("Missing Required Options");
      alert.setResizable(false);
      alert.showAndWait();
    }
  }
  
  /**
   * Verifies a RawArgumentForm and produces
   * a mapping of Options and their arguments if the form
   * is valid (all required arguments are provided and all
   * provided arguments were verified)
   * @param formToProcess - the RawArgumentForm to process
   * @return a Map<String, String> with keys being Option names and
   *         values being the String-valued argument to the corresponding Option
   * @throws IncompleteException - if any of the conditions are true:
   *         * There missing arguments to required Options
   *         * There are arguments that remain unverified
   */
  private Map<String, String> processArgs(RawArgumentForm formToProcess) throws IncompleteException {
    final Map<String, String> args = new HashMap<>();
    final Set<String> givenRequiredOptions = new HashSet<>();
    
    for (Entry<Option, Value> argVal: formToProcess.getRawArguments().entrySet()) {
      
      LOG.info("--- processing: "+argVal.getKey()+" <-> "+argVal.getValue());
      
      if (argVal.getValue().getValue() != null && 
          !argVal.getValue().getValue().isEmpty() && 
          argVal.getValue().isVerified()) {
        args.put(argVal.getKey().getOptName(), argVal.getValue().getValue());
        
        LOG.info("    -> valid :D");
        
        if (argVal.getKey().isRequired()) {
          givenRequiredOptions.add(argVal.getKey().getOptName());
        }
      }
    }
    
    final Set<String> requiredOptions = new HashSet<>(formToProcess.getSubcommand().getRequiredOptions().keySet());
    LOG.info("---  given required options "+givenRequiredOptions);
    if (!givenRequiredOptions.equals(requiredOptions)) {
      
      LOG.info(givenRequiredOptions.toString());
      
      requiredOptions.removeAll(givenRequiredOptions);
      throw new IncompleteException(formToProcess.getSubcommand(), requiredOptions);
    }
    
    return args;
  }
  
  /**
   * Generates the header of this UI.
   * 
   * The header contains the overall description of the
   * program, as well as the program's icon - if provided.
   * @return a Pane representing this UI's header
   */
  private Pane generateHeader() {
    final HBox header = new HBox(10);
    header.setId(FormConstants.HEADER_PANE);
    
    LOG.info("---header");
    LOG.info(header.getStyle()+"||");
    
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
      
      //set the image as the program's icon to display on the application's header
      content.getIcons().add(program.getImage());
    }
    
    return header;
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
