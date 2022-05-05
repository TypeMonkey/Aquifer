package jg.guifier.commands;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;
import jg.guifier.commands.options.Option;

/**
 * A program is a collection of Options and Subcommands.
 * 
 * The program itself is represented as a Subcommand - which can be retrieved using getProgramOptions().
 * This instance can be used to add Options specific to the program itself.
 * 
 * @author Jose Guaro
 */
public class Program {
  
  private String programName;
  private String description;
  private Image image;
  
  private Map<String, Subcommand> subcommands;

	public Program(String name, String description) {
	  this.programName = name;
	  this.description = description;
	  this.subcommands = new HashMap<>();
	  
	  /**
	   * We'll need to internally represent the program command handle 
	   * as a "subcommand" with the name being the program's name.
	   */
	  final Subcommand programRepresentation = new Subcommand(name);
	  programRepresentation.setDescription(description);
	  subcommands.put(name, programRepresentation);
	}
	
	/**
	 * Sets the icon to display on the GUI.
	 * 
	 * Note: This image will be resized to 40x40 pixels (ratio preserved)
	 * 
	 * @param image - the program icon
	 */
	public void setIcon(Image image) {
	  this.image = image;
	}
	
	public void setDescription(String description) {
    this.description = description;
  }
	
	public void addProgramOption(Option option) {
	  subcommands.get(programName).addOption(option);
	}
	
	public Subcommand getProgramOptions() {
	  return subcommands.get(programName);
	}
	
	public boolean addSubcommand(Subcommand subcommand) {
	  if (!subcommands.containsKey(subcommand.getName())) {
      subcommands.put(subcommand.getName(), subcommand);
      return true;
    }
	  return false;
	}
	
	public Map<String, Subcommand> getSubcommands() {
    return subcommands;
  }
	
	public String getDescription() {
    return description;
  }
	
	public String getName() {
    return programName;
  }
	
	public Image getImage() {
    return image;
  }
}
