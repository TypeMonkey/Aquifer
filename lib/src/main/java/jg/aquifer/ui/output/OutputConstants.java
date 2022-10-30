package jg.aquifer.ui.output;

import java.util.function.Function;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Each important Node in OutputPane has
 * a unique ID that can be looked up for manipulation.
 * 
 * These IDs are stored here.
 */
public interface OutputConstants {

  /**
   * ID of the Pane that represents the header of OutputPane
   */
  public static final String HEADER_PANE = "headerPane";
  public static final Function<OutputPane, Pane> HEADER_PANE_FUNC = (node) -> {
    return (Pane) node.lookup("#"+HEADER_PANE);
  };

  /**
   * ID of the VBox that holds the output and input text areas
   */
  public static final String CONTENT_PANE = "contentPane";
  public static final Function<OutputPane, VBox> CONTENT_PANE_FUNC = (node) -> {
    return (VBox) node.lookup("#"+CONTENT_PANE);
  };

  /**
   * ID of the SplitPane that controls the user-comamnded
   * expansion of both the input and output panes.
   */
  public static final String SPLIT_PANE = "splitPane";
  public static final Function<OutputPane, SplitPane> SPLIT_PANE_FUNC = (node) -> {
    return (SplitPane) node.lookup("#"+SPLIT_PANE);
  };

  /**
   * ID of the AnchorPane that holds the output TextArea ScrollPane
   * 
   * Note: this TextArea is contained in the SplitPane ID'd by
   *       SPLIT_PANE below
   */
  public static final String OUTPUT_PANE = "outputPane";
  public static final Function<OutputPane, AnchorPane> OUTPUT_PANE_FUNC = (node) -> {
    return (AnchorPane) SPLIT_PANE_FUNC.apply(node).getItems().get(0);
  };

  /**
   * ID of the AnchorPane that holds the input TextArea ScrollPane
   * 
   * Note: this TextArea is contained in the SplitPane ID'd by
   *       SPLIT_PANE below
   */
  public static final String INPUT_PANE = "inputPane";
  public static final Function<OutputPane, AnchorPane> INPUT_PANE_FUNC = (node) -> {
    return (AnchorPane) SPLIT_PANE_FUNC.apply(node).getItems().get(1);
  };

  /**
   * ID of the AnchorPane that holds the output ScrollPane
   * 
   * Note: this ScrollPane is contained in the SplitPane ID'd by
   *       SPLIT_PANE below
   */
  public static final String OUTPUT_SCROLL_PANE = "outputScrollPane";
  public static final Function<OutputPane, ScrollPane> OUTPUT_SCROLL_PANE_FUNC = (node) -> {
    return (ScrollPane) OUTPUT_PANE_FUNC.apply(node).lookup("#"+OUTPUT_SCROLL_PANE);
  };

  /**
   * ID of the AnchorPane that holds the input ScrollPane
   * 
   * Note: this ScrollPane is contained in the SplitPane ID'd by
   *       SPLIT_PANE below
   */
  public static final String INPUT_SCROLL_PANE = "inputScrollPane";
  public static final Function<OutputPane, ScrollPane> INPUT_SCROLL_PANE_FUNC = (node) -> {
    return (ScrollPane) INPUT_PANE_FUNC.apply(node).lookup("#"+INPUT_SCROLL_PANE);
  };

  /**
   * ID of the TextArea for program output
   * 
   * Note: this TextArea is contained in the SplitPane ID'd by
   *       SPLIT_PANE below
   */
  public static final String OUTPUT_AREA = "outputArea";
  public static final Function<OutputPane, TextArea> OUTPUT_AREA_FUNC = (node) -> {
    return (TextArea) OUTPUT_SCROLL_PANE_FUNC.apply(node).getContent();
  };

  /**
   * ID of the TextArea for input to the program
   * 
   * Note: this TextArea is contained in the SplitPane ID'd by
   *         SPLIT_PANE below
   */
  public static final String INPUT_AREA = "inputArea";
  public static final Function<OutputPane, TextArea> INPUT_AREA_FUNC = (node) -> {
    return (TextArea) INPUT_SCROLL_PANE_FUNC.apply(node).getContent();
  };

  /**
   * ID of the HBox that represents the footer section of the OutputPane
   */
  public static final String FOOTER = "footer";
  public static final Function<Node, HBox> FOOTER_FUNC = (node) -> {
    return (HBox) node.lookup("#"+FOOTER);
  };

  /**
   * ID of the "Cancel" Button in the OutputPane's footer
   */
  public static final String CANCEL_BUTTON = "cancelButton";
  public static final Function<Node, Button> CANCEL_BUTTON_FUNC = (node) -> {
    return (Button) node.lookup("#"+CANCEL_BUTTON);
  };
}
