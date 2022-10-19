package jg.aquifer.ui;

import java.util.Set;
import java.util.stream.Collectors;

import jg.aquifer.commands.Subcommand;

/**
 * Thrown when not all required options haven't been supplied.
 * @author Jose Guaro
 */
public class IncompleteException extends Exception {
  
  private final Subcommand subcommand;
  private final Set<String> missing;

  /**
   * Constructs an IncompleteException
   * @param subcommand - the Subcommand with incomplete required options
   * @param missingRequiredOptions - the names of the required, incomplete options
   */
  public IncompleteException(Subcommand subcommand, Set<String> missingRequiredOptions) {
    super("Missing required options for '"+subcommand.getName()+"': "+
                       missingRequiredOptions.stream().collect(Collectors.joining(",")));
    
    this.subcommand = subcommand;
    this.missing = missingRequiredOptions;
  }
  
  public Subcommand getSubcommand() {
    return subcommand;
  }
  
  public Set<String> getMissing() {
    return missing;
  }
}
