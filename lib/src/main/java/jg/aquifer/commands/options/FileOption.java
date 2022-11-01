package jg.aquifer.commands.options;

import java.io.File;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import jg.aquifer.commands.Subcommand;
import jg.aquifer.commands.Verifier;
import jg.aquifer.ui.RawArgumentForm;
import jg.aquifer.ui.ValueStatus;

/**
 * A FileOption is a light extension of Option that
 * allows users to choose a file or directory as an argument.
 * 
 * Visually, the user has the option to manually enter a file's/directory's path
 * as their argument, or choose to open their OS file viewer to choose a file/directory.
 * 
 * @author Jose
 */
public class FileOption extends Option {
  
  private FileChooser fileChooser;
  
  /**
   * Constructs an FileOption from a base Option
   * @param option - the base Option
   * @param startingDirectory - the directory to start the file chooser at
   * @param extensionFilters - the file extension filters to filter file listings by
   */
  public FileOption(Option option,
                    File startingDirectory,
                    ExtensionFilter ... extensionFilters) {
    this(option.getOptName(), option.getDescription(), option.isRequired(), option.getVerifier(), startingDirectory, extensionFilters);
  }

  /**
   * Constructs an FileOption with 
   * @param optName - the name of this FileOption
   * @param description - the description of this FileOption
   * @param isRequired - whether this FileOption is required
   * @param verifier - the Verifier to use to validate arguments to this FileOption
   * @param startingDirectory - the directory to start the file chooser at
   * @param extensionFilters - the file extension filters to filter file listings by
   */
  public FileOption(String optName, 
                    String description, 
                    boolean isRequired, 
                    Verifier verifier,
                    File startingDirectory,
                    ExtensionFilter ... extensionFilters) {
    super(optName, description, isRequired, verifier);
    
    this.fileChooser = new FileChooser();
    fileChooser.setTitle("Browse...");
    fileChooser.getExtensionFilters().addAll(extensionFilters);
    fileChooser.setInitialDirectory(startingDirectory);
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
    
    final HBox entryCellHBox = new HBox(5);
    final TextField argEntry = new TextField("Enter file path");
    
    argEntry.setOnMouseClicked(new EventHandler<Event>() {
      private boolean justInitialized = true;
      
      @Override
      public void handle(Event event) {
        if (justInitialized) {
          argEntry.setText("");
          justInitialized = false;
        }
      }
    });
    
    final Label exceptionLabel = new Label();
    exceptionLabel.setTextFill(Color.RED);
    
    argEntry.textProperty().addListener((observable, oldValue, newValue) -> {       
      if (!newValue.isEmpty()) {
        try {
          getVerifier().verify(this, argumentForm, newValue);
          exceptionLabel.setText("");
          setValue(newValue);
          mainCellLayout.getChildren().remove(exceptionLabel);
        } catch (VerificationException e) {
          if(!mainCellLayout.getChildren().contains(exceptionLabel)) {
            exceptionLabel.setText(e.getMessage());
            mainCellLayout.getChildren().add(exceptionLabel);
          }
          
          setValue(newValue, e);
        }
      }
      else {
        setValue(newValue, new VerificationException("Can't be empty!"));
      }
    });
    entryCellHBox.getChildren().add(argEntry);
    
    final Button browseFilesButton = new Button("Browse...");
    browseFilesButton.setOnAction((event) -> {
      File chosen = fileChooser.showOpenDialog(((Node) event.getTarget()).getScene().getWindow());
      argEntry.setText(chosen == null ? "" : chosen.getAbsolutePath());
    });
    entryCellHBox.getChildren().add(browseFilesButton);
    
    
    mainCellLayout.getChildren().add(entryCellHBox);
    //mainCellLayout.getChildren().add(exceptionLabel);
    
    return mainCellLayout;
  }
}
