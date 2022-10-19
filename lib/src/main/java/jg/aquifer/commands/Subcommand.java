package jg.aquifer.commands;

import java.util.HashMap;
import java.util.Map;

import jg.aquifer.commands.options.Option;

/**
 * A Subcommand is a collection of Options that are
 * specifically meant for that subcommand.
 * 
 * For example, "commit", "add" and "push" can be seen
 * as subcommands of "git".
 * 
 * Each Option under a Subcommand must be unique in name.
 * 
 * @author Jose Guaro
 */
public class Subcommand {

  private final String name;
  private String description;

  private Map<String, Option> requiredOptions;
  private Map<String, Option> allOptions;
  
  /**
   * Constructs a Subcommand
   * @param name - the name of this Subcommand
   */
  public Subcommand(String name) {
    this.name = name;
    this.description = "";
    this.requiredOptions = new HashMap<>();
    this.allOptions = new HashMap<>();
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Subcommand) {
      return ((Subcommand) obj).name.equals(name);
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return name.hashCode();
  }
  
  /**
   * Checks whether this Subcommand contains an 
   * Option with the given name
   * @param optName - the name of the Option to check for
   * @return true if this Subcommand contains an Option with optName as its name,
   *         false if else
   */
  public boolean hasOption(String optName) {
    return allOptions.containsKey(optName);
  }
  
  /**
   * Checks whether this Subcommand contains a
   * required Option with the given name
   * @param optName - the name of the required Option to check for
   * @return true if this Subcommand contains a required Option with optName as its name,
   *         false if else
   */
  public boolean isOptionRequired(String optName) {
    return requiredOptions.containsKey(optName);
  }
  
  /**
   * Sets the description of this Subcommand
   * @param description - the description of this Subcommand
   */
  public void setDescription(String description) {
    this.description = description;
  }
  
  /**
   * Adds the provided Option to this Subcommand
   * @param option - the Option to add
   * 
   * Note: if an Option with the same name as the one provided
   *       already exists in this Subcommand, the old Option is
   *       overwritten with the provided Option
   */
  public void addOption(Option option) {
    if (option.isRequired()) {
      requiredOptions.put(option.getOptName(), option);
    }
    allOptions.put(option.getOptName(), option);
  }
  
  public String getDescription() {
    return description;
  }
  
  public Map<String, Option> getOptions() {
    return allOptions;
  }
  
  public Map<String, Option> getRequiredOptions() {
    return requiredOptions;
  }
  
  public String getName() {
    return name;
  }
  
  @Override
  public String toString() {
    return name;
  }
}
