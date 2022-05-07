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
  
  public boolean hasOption(String optName) {
    return allOptions.containsKey(optName);
  }
  
  public boolean isOptionRequired(String optName) {
    return requiredOptions.containsKey(optName);
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
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
