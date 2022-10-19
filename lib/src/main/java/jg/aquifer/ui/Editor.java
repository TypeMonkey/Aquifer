package jg.aquifer.ui;

import jg.aquifer.ui.form.FormPane;
import jg.aquifer.ui.output.OutputPane;

/**
 * Allows for custom editions to the generated UI, such as 
 * adding various event listeners to UI components,
 * adding new Nodes, etc.
 */
@FunctionalInterface
public interface Editor {
  
  public void edit(FormPane formPane, OutputPane outputPane);

}
