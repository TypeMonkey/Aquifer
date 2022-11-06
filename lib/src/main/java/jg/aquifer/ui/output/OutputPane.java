package jg.aquifer.ui.output;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jg.aquifer.ui.io.IO;
import jg.aquifer.ui.io.UIPrinter;

/**
 * Upon a successful submission of arguments, the UI
 * will display this pane. This class holds the logic regarding
 * that pane's design, layout and triggers.
 */
public class OutputPane extends AnchorPane implements OutputConstants {
  private static final Logger LOG = LoggerFactory.getLogger(OutputPane.class);

  private final Pane headerPane;
  private final IO streams;
  private final Runnable closeOperation;
  
  public OutputPane(Pane headerPane, Runnable closeOperation) throws IOException {
    this.headerPane = headerPane;
    this.streams = applyLayout();
    this.closeOperation = closeOperation;
  }

  private IO applyLayout() throws IOException {
    final VBox mainContentVBox = new VBox();
    mainContentVBox.setId(CONTENT_PANE);
    AnchorPane.setBottomAnchor(mainContentVBox, 0.0);
    AnchorPane.setTopAnchor(mainContentVBox, 0.0);
    AnchorPane.setLeftAnchor(mainContentVBox, 0.0);
    AnchorPane.setRightAnchor(mainContentVBox, 0.0);

    //Our "content pane"
    getChildren().add(mainContentVBox);

    //Add headerPane first
    mainContentVBox.getChildren().add(headerPane);

    /*
      * There are two TextAreas:
      * - outputArea: displays program outputs
      * - inputArea: takes in multi-line user input to 
      *              pass to the program
      */
    final TextArea outputArea = new TextArea();
    outputArea.setId(OUTPUT_AREA);
    final TextArea inputArea = new TextArea();
    inputArea.setId(INPUT_AREA);

    //Create IO object for program to use streams for both TextAreas
    final IO streams = generateStreams(outputArea, inputArea);

    //Create the pane for displaying program output
    final AnchorPane outputPane = createOutputPane(outputArea);
    //mainContentVBox.getChildren().add(outputPane);

    //Create the pane for program input
    final AnchorPane inputPane = createInputPane(inputArea);
    //mainContentVBox.getChildren().add(inputPane);

    final SplitPane splitPane = new SplitPane(outputPane, inputPane);
    splitPane.setId(SPLIT_PANE);
    splitPane.setOrientation(Orientation.VERTICAL);
    splitPane.setDividerPosition(0, .75);
    mainContentVBox.getChildren().add(splitPane);

    //The code below will be laying out the footer.

    //Create the "Cancel" button
    final Button cancelButton = new Button("Cancel");
    cancelButton.setId(CANCEL_BUTTON);

    cancelButton.setOnAction((event) -> {
      Alert alert = new Alert(AlertType.CONFIRMATION, 
                              "Are you sure you want to cancel?", 
                              ButtonType.YES, ButtonType.NO);
      alert.setTitle("Cancel");
      alert.setResizable(false);
      alert.showAndWait();
      
      if (alert.getResult() == ButtonType.YES) {
          /*
          Platform.exit();
          System.exit(1);
          */
          closeOperation.run();
      }
    });
    cancelButton.setPrefWidth(60);

    HBox.setMargin(cancelButton, new Insets(10));
    HBox.setHgrow(cancelButton, Priority.ALWAYS);
    
    final HBox footer = new HBox(cancelButton);
    footer.setId(FOOTER);
    footer.setAlignment(Pos.CENTER);
    
    mainContentVBox.getChildren().add(footer);
    
    return streams;
  }

  private AnchorPane createOutputPane(TextArea outputArea) {
    //Create the TextArea (and associated components) for program output
    final AnchorPane outputBodyPane = new AnchorPane();
    outputBodyPane.setId(OUTPUT_PANE);

    outputArea.setWrapText(true);
    outputArea.setEditable(false);
          
    /*
    this.outputAppendFunc = new Callback<String, Void>() {
      @Override
      public Void call(String param) {
        int caretPosition = outputArea.caretPositionProperty().get();
        outputArea.appendText(param+System.lineSeparator());
        outputArea.positionCaret(caretPosition);
        return null;
      }
    };
    */
    
    final ScrollPane outputAreaScrollPane = new ScrollPane(outputArea);
    outputAreaScrollPane.setId(OUTPUT_SCROLL_PANE);
    outputAreaScrollPane.setFitToHeight(true);
    outputAreaScrollPane.setFitToWidth(true);
    
    //Set starting height
    outputAreaScrollPane.setPrefHeight(500);
    
    outputArea.setScrollTop(25);

    AnchorPane.setBottomAnchor(outputArea, 0.0);
    AnchorPane.setTopAnchor(outputArea, 0.0);
    AnchorPane.setLeftAnchor(outputArea, 0.0);
    AnchorPane.setRightAnchor(outputArea, 0.0);
    
    AnchorPane.setBottomAnchor(outputAreaScrollPane, 0.0);
    AnchorPane.setTopAnchor(outputAreaScrollPane, 0.0);
    AnchorPane.setLeftAnchor(outputAreaScrollPane, 0.0);
    AnchorPane.setRightAnchor(outputAreaScrollPane, 0.0);
    
    VBox.setVgrow(outputBodyPane, Priority.ALWAYS);
    
    outputAreaScrollPane.setPadding(new Insets(10));
    outputBodyPane.getChildren().add(outputAreaScrollPane);
            
    return outputBodyPane;
  }

  private AnchorPane createInputPane(TextArea inputArea) {
    //Create the TextArea (and associated components) for program input
    final AnchorPane inputBodyPane = new AnchorPane();
    inputBodyPane.setId(INPUT_PANE);

    inputArea.setWrapText(true);
    inputArea.setEditable(true);
          
    /*
    this.outputAppendFunc = new Callback<String, Void>() {
      @Override
      public Void call(String param) {
        int caretPosition = outputArea.caretPositionProperty().get();
        outputArea.appendText(param+System.lineSeparator());
        outputArea.positionCaret(caretPosition);
        return null;
      }
    };
    */
    
    final ScrollPane inputAreaScrollPane = new ScrollPane(inputArea);
    inputAreaScrollPane.setId(INPUT_SCROLL_PANE);

    inputAreaScrollPane.setFitToHeight(true);
    inputAreaScrollPane.setFitToWidth(true);
    
    //Set starting height
    inputAreaScrollPane.setPrefHeight(75);
    
    //outputArea.setScrollTop(25);

    AnchorPane.setBottomAnchor(inputArea, 0.0);
    AnchorPane.setTopAnchor(inputArea, 0.0);
    AnchorPane.setLeftAnchor(inputArea, 0.0);
    AnchorPane.setRightAnchor(inputArea, 0.0);
    
    AnchorPane.setBottomAnchor(inputAreaScrollPane, 0.0);
    AnchorPane.setTopAnchor(inputAreaScrollPane, 0.0);
    AnchorPane.setLeftAnchor(inputAreaScrollPane, 0.0);
    AnchorPane.setRightAnchor(inputAreaScrollPane, 0.0);
    
    VBox.setVgrow(inputBodyPane, Priority.ALWAYS);
    
    inputAreaScrollPane.setPadding(new Insets(10));
    inputBodyPane.getChildren().add(inputAreaScrollPane);
            
    //Add Output textarea to content pane
    return inputBodyPane;
  }

  private IO generateStreams(TextArea outputArea, TextArea inputArea) throws IOException {  
    try {
      final PrintWriter stdPrintOut = new PrintWriter(new UIPrinter(outputArea));

      final PipedOutputStream textAreaDestStream = new PipedOutputStream();
      final PipedInputStream inputStream = new PipedInputStream(textAreaDestStream);

      inputArea.setOnKeyPressed( (event) -> {
        if (event.getCode() == KeyCode.ENTER) {
          final String input = inputArea.getText();
          //Clear input area
          inputArea.clear();

          if (input != null && !input.isEmpty()) {
            try {
              //write to standard out too
              stdPrintOut.println(input);
              textAreaDestStream.write(input.getBytes());
            } catch (IOException e) {
              LOG.error("Fatal error: Writing to PipedOutputStream causes IOException", e);
              throw new Error("Fatal error: Writing to PipedOutputStream causes IOException. Bubbling up!", e);
            }
          }

          /*
            * Without this, for some reason, previous inputs
            * are reprinted to the output textarea.
            */
          event.consume();
        }
      });

      return new IO(stdPrintOut, stdPrintOut, inputStream);   
    } catch (IOException e) {
      LOG.error("Fatal error: Making input streams for output scene causes IOException. Bubbling up!", e);
      throw e;
    }
  }

  public IO getStreams() {
    return streams;
  }
}
