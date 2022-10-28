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
import javafx.scene.text.Text;
import jg.aquifer.ui.form.FormConstants;

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
  public static final Function<Node, Pane> HEADER_PANE_FUNC = FormConstants.HEADER_PANE_FUNC;

  /**
   * ID of the VBox that holds the output and input text areas
   */
  public static final String CONTENT_PANE = "contentPane";
  public static final Function<Node, VBox> CONTENT_PANE_FUNC = (node) -> {
    return (VBox) node.lookup("#"+CONTENT_PANE);
  };

  /**
   * ID of the TextArea for program output
   * 
   * Note: this TextArea is contained in the SplitPane ID'd by
   *       SPLIT_PANE below
   */
  public static final String OUTPUT_AREA = "outputArea";

  /**
   * ID of the TextArea for input to the program
   * 
   * Note: this TextArea is contained in the SplitPane ID'd by
   *         SPLIT_PANE below
   */
  public static final String INPUT_AREA = "inputArea";

  /**
   * ID of the AnchorPane that holds the output TextArea ScrollPane
   * 
   * Note: this TextArea is contained in the SplitPane ID'd by
   *       SPLIT_PANE below
   */
  public static final String OUTPUT_PANE = "outputPane";

  /**
   * ID of the SplitPane that controls the user-comamnded
   * expansion of both the input and output panes.
   */
  public static final String SPLIT_PANE = "splitPane";
  public static final Function<Node, SplitPane> SPLIT_PANE_FUNC = (node) -> {
    return (SplitPane) node.lookup("#"+SPLIT_PANE);
  };

  /**
   * ID of the AnchorPane that holds the output ScrollPane
   * 
   * Note: this ScrollPane is contained in the SplitPane ID'd by
   *       SPLIT_PANE below
   */
  public static final String OUTPUT_SCROLL_PANE = "outputScrollPane";

  /**
   * ID of the AnchorPane that holds the input TextArea ScrollPane
   * 
   * Note: this TextArea is contained in the SplitPane ID'd by
   *       SPLIT_PANE below
   */
  public static final String INPUT_PANE = "inputPane";

  /**
   * ID of the AnchorPane that holds the input ScrollPane
   * 
   * Note: this ScrollPane is contained in the SplitPane ID'd by
   *       SPLIT_PANE below
   */
  public static final String INPUT_SCROLL_PANE = "inputScrollPane";

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
