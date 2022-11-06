package jg.aquifer.ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jg.aquifer.commands.Program;
import jg.aquifer.commands.Subcommand;
import jg.aquifer.commands.Verifier;
import jg.aquifer.commands.options.Flag;
import jg.aquifer.commands.options.Option;
import jg.aquifer.commands.options.RadioOption;
import jg.aquifer.ui.form.FormConstants;
import jg.aquifer.ui.form.FormPane;
import jg.aquifer.ui.output.OutputConstants;
import jg.aquifer.ui.output.OutputPane;

public class UIConstantsTest extends Application {
  static Program program;
  static FormPane formPane;
  static OutputPane outputPane;
  static volatile boolean initDone = false;

  @BeforeAll
  public static void setUp() {
    program = new Program("sampleProg", "This is a sample program to test things out");

    for(int i = 1; i < 4; i++) {
        program.addSubcommand(new Subcommand("other"+i));
    }
    
    Subcommand subcommand = new Subcommand("withdesc");
    subcommand.setDescription("This is a subcommand for the program");  
    subcommand.addOption(new Option("minus", "Removes a specific file", true, Verifier.WHOLE_NUM));
    
    program.addSubcommand(subcommand);
    
    program.addProgramOption(new Option("add", "Adds files in the current directory"));
    program.addProgramOption(new Option("commit", "Commits files in the current directory"));
    program.addProgramOption(new Option("push", "Pushes files in the current directory"));
    
    program.addProgramOption(new Option("amend", "Amends files in the current directory", true));
    program.addProgramOption(new RadioOption("multiradio", "Multiple files in the current directory", true, "option1", "option2", "option3"));
    //program.addProgramOption(new FileOption("multi", "Multiple files in the current directory", true, Verifier.STR_VERIFIER, new File(System.getProperty("user.home"))));
    
    program.addProgramOption(new Flag("allowDups", "Allows duplicate files"));
    program.addProgramOption(new Flag("flag2", "2Allows duplicate files"));
    program.addProgramOption(new Flag("flag3", "3Allows duplicate files"));
    program.addProgramOption(new Flag("flag4", "4Allows duplicate files"));


    //Starts JavaFX platform
    new JFXPanel();

    Platform.runLater(() -> {
      try {
        new UIConstantsTest().start(new Stage());
      } catch (Exception e) {
        fail("Couldn't initialize JavaFX thread, "+e.getMessage());
      }
    });

    /*
     * Ugly spinlock that waits for Visualizer to complete initialization
     */
    while (!initDone);
    System.out.println("--done!");
  }

  @Test
  public void testOutputFuncs() {
    assertInstanceOf(Pane.class, OutputConstants.HEADER_PANE_FUNC.apply(outputPane));
    assertEquals(OutputConstants.HEADER_PANE, OutputConstants.HEADER_PANE_FUNC.apply(outputPane).getId());

    assertInstanceOf(VBox.class, OutputConstants.CONTENT_PANE_FUNC.apply(outputPane));
    assertEquals(OutputConstants.CONTENT_PANE, OutputConstants.CONTENT_PANE_FUNC.apply(outputPane).getId());

    assertInstanceOf(TextArea.class, OutputConstants.OUTPUT_AREA_FUNC.apply(outputPane));
    assertEquals(OutputConstants.OUTPUT_AREA, OutputConstants.OUTPUT_AREA_FUNC.apply(outputPane).getId());

    assertInstanceOf(TextArea.class, OutputConstants.INPUT_AREA_FUNC.apply(outputPane));
    assertEquals(OutputConstants.INPUT_AREA, OutputConstants.INPUT_AREA_FUNC.apply(outputPane).getId());

    assertInstanceOf(AnchorPane.class, OutputConstants.OUTPUT_PANE_FUNC.apply(outputPane));
    assertEquals(OutputConstants.OUTPUT_PANE, OutputConstants.OUTPUT_PANE_FUNC.apply(outputPane).getId());

    assertInstanceOf(SplitPane.class, OutputConstants.SPLIT_PANE_FUNC.apply(outputPane));
    assertEquals(OutputConstants.SPLIT_PANE, OutputConstants.SPLIT_PANE_FUNC.apply(outputPane).getId());

    assertInstanceOf(ScrollPane.class, OutputConstants.OUTPUT_SCROLL_PANE_FUNC.apply(outputPane));
    assertEquals(OutputConstants.OUTPUT_SCROLL_PANE, OutputConstants.OUTPUT_SCROLL_PANE_FUNC.apply(outputPane).getId());

    assertInstanceOf(AnchorPane.class, OutputConstants.INPUT_PANE_FUNC.apply(outputPane));
    assertEquals(OutputConstants.INPUT_PANE, OutputConstants.INPUT_PANE_FUNC.apply(outputPane).getId());

    assertInstanceOf(ScrollPane.class, OutputConstants.INPUT_SCROLL_PANE_FUNC.apply(outputPane));
    assertEquals(OutputConstants.INPUT_SCROLL_PANE, OutputConstants.INPUT_SCROLL_PANE_FUNC.apply(outputPane).getId());

    assertInstanceOf(HBox.class, OutputConstants.FOOTER_FUNC.apply(outputPane));
    assertEquals(OutputConstants.FOOTER, OutputConstants.FOOTER_FUNC.apply(outputPane).getId());

    assertInstanceOf(Button.class, OutputConstants.CANCEL_BUTTON_FUNC.apply(outputPane));
    assertEquals(OutputConstants.CANCEL_BUTTON, OutputConstants.CANCEL_BUTTON_FUNC.apply(outputPane).getId());
  }

  @Test
  public void testFormFuncs() {
    assertInstanceOf(Pane.class, FormConstants.HEADER_PANE_FUNC.apply(formPane));
    assertEquals(FormConstants.HEADER_PANE, FormConstants.HEADER_PANE_FUNC.apply(formPane).getId());

    assertInstanceOf(StackPane.class, FormConstants.CONTENT_PANE_FUNC.apply(formPane));
    assertEquals(FormConstants.CONTENT_PANE, FormConstants.CONTENT_PANE_FUNC.apply(formPane).getId());

    assertInstanceOf(VBox.class, FormConstants.CONTENT_VBOX_FUNC.apply(formPane));
    assertEquals(FormConstants.CONTENT_VBOX, FormConstants.CONTENT_VBOX_FUNC.apply(formPane).getId());

    assertInstanceOf(HBox.class, FormConstants.FOOTER_FUNC.apply(formPane));
    assertEquals(FormConstants.FOOTER, FormConstants.FOOTER_FUNC.apply(formPane).getId());

    assertInstanceOf(Button.class, FormConstants.RUN_BUTTON_FUNC.apply(formPane));
    assertEquals(FormConstants.RUN_BUTTON, FormConstants.RUN_BUTTON_FUNC.apply(formPane).getId());

    assertInstanceOf(Button.class, FormConstants.CANCEL_BUTTON_FUNC.apply(formPane));
    assertEquals(FormConstants.CANCEL_BUTTON, FormConstants.CANCEL_BUTTON_FUNC.apply(formPane).getId());

    assertInstanceOf(TabPane.class, FormConstants.TAB_PANE_FUNC.apply(formPane));
    assertEquals(FormConstants.TAB_PANE, FormConstants.TAB_PANE_FUNC.apply(formPane).getId());

    TabPane tabPane = FormConstants.TAB_PANE_FUNC.apply(formPane);
    assertEquals(program.getSubcommands().size(), tabPane.getTabs().size());
    for (Tab tab : tabPane.getTabs()) {
      final Subcommand subcommand = program.getSubcommands().get(tab.getText());
      assertNotNull(subcommand);

      final String TAB_ID = subcommand.getName()+FormConstants.TAB_SUFFIX;
      assertEquals(TAB_ID, tab.getId());

      assertInstanceOf(AnchorPane.class, tab.getContent());
      assertEquals(TAB_ID+FormConstants.TAB_ANCHOR_SUFFIX, tab.getContent().getId());

      final AnchorPane anchoredContent = (AnchorPane) tab.getContent();
      assertEquals(anchoredContent.getChildren().size(), 1);
      assertInstanceOf(VBox.class, anchoredContent.getChildren().get(0));
      assertEquals(TAB_ID+FormConstants.TAB_CONTENT_SUFFIX, anchoredContent.getChildren().get(0).getId());

      final VBox paneVBox = (VBox) anchoredContent.getChildren().get(0);
      SplitPane splitPane = null;
      if (subcommand.getDescription() != null && !subcommand.getDescription().isEmpty()) {
        //Subcommand has a description
        System.out.println(paneVBox.getChildren());
        assertEquals(3, paneVBox.getChildren().size());
        assertInstanceOf(Label.class, paneVBox.getChildren().get(0));
        assertInstanceOf(SplitPane.class, paneVBox.getChildren().get(1));
        assertInstanceOf(Pane.class, paneVBox.getChildren().get(2));

        assertEquals(TAB_ID+FormConstants.SPLIT_PANE_SUFFIX, paneVBox.getChildren().get(1).getId());
        splitPane = (SplitPane) paneVBox.getChildren().get(1);
      }
      else {
        assertEquals(2, paneVBox.getChildren().size());
        assertInstanceOf(SplitPane.class, paneVBox.getChildren().get(0));
        assertInstanceOf(Pane.class, paneVBox.getChildren().get(1));

        assertEquals(TAB_ID+FormConstants.SPLIT_PANE_SUFFIX, paneVBox.getChildren().get(0).getId());
        splitPane = (SplitPane) paneVBox.getChildren().get(0);
      }

      assertNotNull(splitPane);
      assertEquals(3, splitPane.getItems().size());

      //Calculate option counts
      int requiredOpts = 0;
      int optionalOpts = 0;
      int flagOpts = 0;

      for (Option opt : subcommand.getOptions().values()) {
        if(opt instanceof Flag) {
          flagOpts++;
        }
        else if (opt.isRequired()) {
          requiredOpts++;
        }
        else {
          optionalOpts++;
        }
      }

      //Check required section
      assertInstanceOf(TitledPane.class, splitPane.getItems().get(0));
      final TitledPane requiredPane = (TitledPane) splitPane.getItems().get(0);
      assertEquals(TAB_ID+FormConstants.TAB_REQ_PANE_SUFFIX, requiredPane.getId());
      if (requiredOpts > 0) {
        assertInstanceOf(ScrollPane.class, requiredPane.getContent());
        assertInstanceOf(FlowPane.class, ((ScrollPane) requiredPane.getContent()).getContent());
        assertEquals(TAB_ID+FormConstants.TAB_REQ_FPANE_SUFFIX, ((ScrollPane) requiredPane.getContent()).getContent().getId());
      }
      else {
        assertInstanceOf(StackPane.class, requiredPane.getContent());
      }


      //Check optional section
      assertInstanceOf(TitledPane.class, splitPane.getItems().get(1));
      final TitledPane optionalPane = (TitledPane) splitPane.getItems().get(1);
      assertEquals(TAB_ID+FormConstants.TAB_OPT_PANE_SUFFIX, optionalPane.getId());
      if (optionalOpts > 0) {
        assertInstanceOf(ScrollPane.class, optionalPane.getContent());
        assertInstanceOf(FlowPane.class, ((ScrollPane) optionalPane.getContent()).getContent());
        assertEquals(TAB_ID+FormConstants.TAB_OPT_FPANE_SUFFIX, ((ScrollPane) optionalPane.getContent()).getContent().getId());
      }
      else {
        assertInstanceOf(StackPane.class, optionalPane.getContent());
      }

      //Check flags section
      assertInstanceOf(TitledPane.class, splitPane.getItems().get(2));
      final TitledPane flagPane = (TitledPane) splitPane.getItems().get(2);
      assertEquals(TAB_ID+FormConstants.TAB_FLAG_PANE_SUFFIX, flagPane.getId());
      if (flagOpts > 0) {
        assertInstanceOf(ListView.class, flagPane.getContent());
        assertEquals(TAB_ID+FormConstants.TAB_FLAG_LIST_SUFFIX, flagPane.getContent().getId());
      }
      else {
        assertInstanceOf(StackPane.class, flagPane.getContent());
      }
    }
  }

  public void uiSetup() {
    Visualizer visualizer = new Visualizer(program, (s, form, streams) -> {});
    try {
      visualizer.initialize();
    } catch (IOException e) {
      fail("Visualizer initialization failed! "+e.getMessage());
    }

    formPane = visualizer.getFormPane();
    outputPane = visualizer.getOutputPane();

    assertNotNull(formPane);
    assertNotNull(outputPane);

    initDone = true;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    uiSetup();
  }

  @AfterAll
  public static void endFX() {
    Platform.exit();
  }
}
