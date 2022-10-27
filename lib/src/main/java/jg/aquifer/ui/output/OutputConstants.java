package jg.aquifer.ui.output;

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

  /**
   * ID of the VBox that holds the output and input text areas
   */
  public static final String CONTENT_PANE = "contentPane";

  /**
   * ID of the TextArea for program output
   */
  public static final String OUTPUT_AREA = "outputArea";

  /**
   * ID of the TextArea for input to the program
   */
  public static final String INPUT_AREA = "inputArea";

  /**
   * ID of the AnchorPane that holds the output TextArea ScrollPane
   */
  public static final String OUTPUT_PANE = "outputPane";

  /**
   * ID of the SplitPane that controls the user-comamnded
   * expansion of both the input and output panes.
   */
  public static final String SPLIT_PANE = "splitPane";

  /**
   * ID of the AnchorPane that holds the output ScrollPane
   */
  public static final String OUTPUT_SCROLL_PANE = "outputScrollPane";

  /**
   * ID of the AnchorPane that holds the input TextArea ScrollPane
   */
  public static final String INPUT_PANE = "inputPane";

  /**
   * ID of the AnchorPane that holds the input ScrollPane
   */
  public static final String INPUT_SCROLL_PANE = "inputScrollPane";

  /**
   * ID of the HBox that represents the footer section of the OutputPane
   */
  public static final String FOOTER = "footer";

  /**
   * ID of the "Cancel" Button in the OutputPane's footer
   */
  public static final String CANCEL_BUTTON = "cancelButton";
}
