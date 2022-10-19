package jg.aquifer.ui.form;

/**
 * Each important Node in FormPane has
 * a unique ID that can be looked up for manipulation.
 * 
 * These IDs are stored here.
 */
public interface FormConstants {

  /**
   * ID of the Pane that represents the header of FormPane
   */
  public static final String HEADER_PANE = "headerPane";
   
  /**
   * ID of the StackPane which is the parent Node
   * of all components on FormPane
   */
  public static final String CONTENT_PANE = "contentPane";

  /**
   * ID of the VBox that organizes the header, body and footer
   * sections of FormPane
   */
  public static final String CONTENT_VBOX = "contentVBox";

  /**
   * ID of the HBox which contains the components of 
   * the FormPane's footer
   */
  public static final String FOOTER = "footerHBox";

  /**
   * ID of the "Run" Button
   */
  public static final String RUN_BUTTON = "runButton";

  /**
   * ID of the "Cancel" Button
   */
  public static final String CANCEL_BUTTON = "cancelButton";

  /**
   * ID of the TabPane that holds all subcommand Tabs
   */
  public static final String TAB_PANE = "tabbedPane";

  /**
   * Each Tab's ID is <subcommand name>Tab
   * 
   * For example, the ID of the "commit" subcommand
   * is "commitTab".
   * 
   * Using this suffix, this should correspond to a Tab
   */
  public static final String TAB_SUFFIX = "Tab";

  /**
   * Each Tab's ID is <subcommand name>Tab
   * 
   * For example, the ID of the "commit" subcommand
   * is "commitTab".
   * 
   * Using this suffix and example, the ID should be 
   * "commitTabContent", which corrsponds to a VBox
   * which holds all the subcompontnets of this subcommand's Tab
   */
  public static final String TAB_CONTENT_SUFFIX = "Content";

  /**
   * Each Tab's ID is <subcommand name>Tab
   * 
   * For example, the ID of the "commit" subcommand
   * is "commitTab".
   * 
   * Using this suffix and example, the ID should be 
   * "commitTabAnchoredContent", which corrsponds to an AnchorPane
   * which holds the Tab's VBox
   */
  public static final String TAB_ANCHOR_SUFFIX = "AnchoredContent";

  /**
   * Each Tab's ID is <subcommand name>Tab
   * 
   * For example, the ID of the "commit" subcommand
   * is "commitTab".
   * 
   * Using this suffix and example, the ID should be 
   * "commitTabReqList", which corresponds to a ListView<Option>
   * which holds the corresponding Subcommand's required Options
   */
  public static final String TAB_REQ_LIST_SUFFIX = "ReqList";

  /**
   * Each Tab's ID is <subcommand name>Tab
   * 
   * For example, the ID of the "commit" subcommand
   * is "commitTab".
   * 
   * Using this suffix and example, the ID should be 
   * "commitTabReqList", which corresponds to a ListView<Option>
   * which holds the corresponding Subcommand's optional/non-Flag Options
   */
  public static final String TAB_OPT_LIST_SUFFIX = "OptList";

  /**
   * Each Tab's ID is <subcommand name>Tab
   * 
   * For example, the ID of the "commit" subcommand
   * is "commitTab".
   * 
   * Using this suffix and example, the ID should be 
   * "commitTabReqList", which corresponds to a ListView<Option>
   * which holds the corresponding Subcommand's Flags
   */
  public static final String TAB_FLAG_LIST_SUFFIX = "FlagList";

  /**
   * Each Tab's ID is <subcommand name>Tab
   * 
   * For example, the ID of the "commit" subcommand
   * is "commitTab".
   * 
   * Using this suffix and example, the ID should be 
   * "commitTabReqPane", which corrsponds to a TitledPane
   * which holds the corresponding required ListView<Option>
   */
  public static final String TAB_REQ_PANE_SUFFIX = "ReqPane";

  /**
   * Each Tab's ID is <subcommand name>Tab
   * 
   * For example, the ID of the "commit" subcommand
   * is "commitTab".
   * 
   * Using this suffix and example, the ID should be 
   * "commitTabReqPane", which corrsponds to a TitledPane
   * which holds the corresponding optional/non-Flag ListView<Option>
   */
  public static final String TAB_OPT_PANE_SUFFIX = "OptPane";

  /**
   * Each Tab's ID is <subcommand name>Tab
   * 
   * For example, the ID of the "commit" subcommand
   * is "commitTab".
   * 
   * Using this suffix and example, the ID should be 
   * "commitTabReqPane", which corrsponds to a TitledPane
   * which holds the corresponding flag ListView<Option>
   */
  public static final String TAB_FLAG_PANE_SUFFIX = "FlagPane";

  public static final int PREF_WIDTH = 400;
  public static final int PREF_HEIGHT = 600;
}
