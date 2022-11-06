package jg.aquifer.ui.form;

import java.util.function.Function;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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
  public static final Function<FormPane, Pane> HEADER_PANE_FUNC = (node) -> {
    return (Pane) node.lookup("#"+HEADER_PANE);
  };
   
  /**
   * ID of the StackPane which is the parent Node
   * of all components on FormPane
   */
  public static final String CONTENT_PANE = "contentPane";
  public static final Function<FormPane, StackPane> CONTENT_PANE_FUNC = (node) -> {
    return (StackPane) node.lookup("#"+CONTENT_PANE);
  };

  /**
   * ID of the VBox that organizes the header, body and footer
   * sections of FormPane
   */
  public static final String CONTENT_VBOX = "contentVBox";
  public static final Function<FormPane, VBox> CONTENT_VBOX_FUNC = (node) -> {
    return (VBox) node.lookup("#"+CONTENT_VBOX);
  };

  /**
   * ID of the HBox which contains the components of 
   * the FormPane's footer
   */
  public static final String FOOTER = "footerHBox";
  public static final Function<FormPane, HBox> FOOTER_FUNC = (node) -> {
    return (HBox) node.lookup("#"+FOOTER);
  };

  /**
   * ID of the "Run" Button
   */
  public static final String RUN_BUTTON = "runButton";
  public static final Function<FormPane, Button> RUN_BUTTON_FUNC = (node) -> {
    return (Button) node.lookup("#"+RUN_BUTTON);
  };

  /**
   * ID of the "Cancel" Button
   */
  public static final String CANCEL_BUTTON = "cancelButton";
  public static final Function<FormPane, Button> CANCEL_BUTTON_FUNC = (node) -> {
    return (Button) node.lookup("#"+CANCEL_BUTTON);
  };

  /**
   * ID of the TabPane that holds all subcommand Tabs
   */
  public static final String TAB_PANE = "tabbedPane";
  public static final Function<FormPane, TabPane> TAB_PANE_FUNC = (node) -> {
    return (TabPane) node.lookup("#"+TAB_PANE);
  };

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
  public static final String TAB_REQ_FPANE_SUFFIX = "ReqFPane";

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
  public static final String TAB_OPT_FPANE_SUFFIX = "OptFPane";

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

/**
   * Each Tab's ID is <subcommand name>Tab
   * 
   * For example, the ID of the "commit" subcommand
   * is "commitTab".
   * 
   * Using this suffix and example, the ID should be 
   * "commitTabSplitPane", which corrsponds to a SplitPane
   */
  public static final String SPLIT_PANE_SUFFIX = "SplitPane";

  public static final int PREF_WIDTH = 400;
  public static final int PREF_HEIGHT = 600;
}
