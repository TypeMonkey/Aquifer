package jg.aquifer.commands;

import java.nio.file.Files;
import java.nio.file.Paths;

import jg.aquifer.commands.options.Option;
import jg.aquifer.commands.options.VerificationException;
import jg.aquifer.ui.RawArgumentForm;

/**
 * Interface for a simple, one-arg method for a specific Option
 * that can verify if a supplied argument is matches that Option's
 * requirements (ie: if the argument is numerical, an accessible and/or valid file path, etc.).
 * 
 * If the supplied argument DOESN'T match an Option's requirements, then 
 * the method verify() should throw a VerificationException with a concise message
 * detailing the Option's requirements.
 * 
 * This interface is meant to be used with VerifiableOption, with each VerifiableOption instance being
 * supplied with it's own Verifier.
 * 
 * @author Jose Guaro
 */
public interface Verifier {
  
  /**
   * Passive Verifier. Will accept any argument.
   */
  public static final Verifier STR_VERIFIER = (op, form, arg) -> {
  };
  
  /**
   * Accepts only negative whole numbers (negative integers). Rejects the number 0.
   */
  public static final Verifier NEG_WHOLE = (op, form, arg) -> {
    try {
      if(Long.parseLong(arg) >= 0) {
        throw new VerificationException("Expected a negative whole number");
      }
    } catch (NumberFormatException e) {
      throw new VerificationException("Expected a negative whole number");
    }
  };
  
  /**
   * Accepts only non-negative whole numbers (positive integers).
   */
  public static final Verifier NON_NEG_WHOLE = (op, form, arg) -> {
    try {
      if(Long.parseLong(arg) < 0) {
        throw new VerificationException("Expected a non-negative whole number");
      }
    } catch (NumberFormatException e) {
      throw new VerificationException("Expected a non-negative whole number");
    }
  };
  
  /**
   * Accepts only whole numbers (integers).
   */
  public static final Verifier WHOLE_NUM = (op, form, arg) -> {
    try {
      Long.parseLong(arg);
    } catch (NumberFormatException e) {
      throw new VerificationException("Expected a whole number");
    }
  };
  
  /**
   * Accepts only negative decimal numbers. Rejects the number 0.
   */
  public static final Verifier NEG_DEC = (op, form, arg) -> {
    try {
      if(Double.parseDouble(arg) >= 0) {
        throw new VerificationException("Expected a negative decimal number");
      }
    } catch (NumberFormatException e) {
      throw new VerificationException("Expected a negative decimal number");
    }
  };
  
  /**
   * Accepts only non-negative decimal numbers.
   */
  public static final Verifier NON_NEG_DEC = (op, form, arg) -> {
    try {
      if(Double.parseDouble(arg) < 0) {
        throw new VerificationException("Expected a non-negative decimal number");
      }
    } catch (NumberFormatException e) {
      throw new VerificationException("Expected a non-negative decimal number");
    }
  };
  
  /**
   * Accepts only decimal numbers.
   */
  public static final Verifier DEC_NUM = (op, form, arg) -> {
    try {
      Double.parseDouble(arg);
    } catch (NumberFormatException e) {
      throw new VerificationException("Expected a decimal number");
    }
  };
  
  /**
   * Accepts only boolean values - strictly 'true' and 'false'
   */
  public static final Verifier BOOL = (op, form, arg) -> {
    if(!arg.trim().equalsIgnoreCase("true") && !arg.trim().equalsIgnoreCase("false")) {
      throw new VerificationException("Expected a boolean value");
    }
  };
  
  /**
   * Accepts only files/directories that exists.
   */
  public static final Verifier FILE_EXISTS = (op, form, arg) -> {
    if (Files.notExists(Paths.get(arg))) {
      throw new VerificationException("The file does not exist.");
    }
    else if (!Files.exists(Paths.get(arg))) {
      throw new VerificationException("The existance of this file can't be verfied.");
    }
  };
  
  /**
   * Accepts only files (not directories) that can be read from
   */
  public static final Verifier FILE_READ = (op, form, arg) -> {
    if (!Files.isReadable(Paths.get(arg))) {
      throw new VerificationException("The file cannot be read");
    }
  };

  /**
   * Accepts only files (not directories) that can be written to
   */
  public static final Verifier FILE_WRITE = (op, form, arg) -> {
    if (!Files.isWritable(Paths.get(arg))) {
      throw new VerificationException("The file cannot be read");
    }
  };
  
  /**
   * Accepts only files (not directories) that can be executed
   */
  public static final Verifier FILE_EXEC = (op, form, arg) -> {
    if (!Files.isExecutable(Paths.get(arg))) {
      throw new VerificationException("The file cannot be read");
    }
  };
  
  /**
   * Verifies whether the provided argument - as a string - matches
   * the requirements of the given Option.
   * @param arg - the supplied argument
   * @param form - the RawArgumentForm that the given Option is filling out
   * @param arg - the argument
   * @throws VerificationException if arg doesn't match 
   */
  public void verify(Option option, RawArgumentForm form, String arg) throws VerificationException;
  
  /**
   * Returns a single Verifier that 
   * iterates over the provided Verifiers and fails at the first VerificationException
   * @param verifiers - the Verifiers to invoke
   * @return a Verifier that fails at the first VerificationException
   */
  public static Verifier all(Verifier ... verifiers) {
    return (op, form, arg) -> {
      for (Verifier v : verifiers) {
        v.verify(op, form, arg);
      }
    };
  }
  
  /**
   * Returns a single Verifier that 
   * iterates over all of the provided Verifiers and collects the message of each VerificationException
   * thrown into a larger VerificationException containing all messages separated by newline
   * @param verifiers - the Verifiers to invoke
   * @return a Verifier that collects the messages of all thrown VerificationExceptions
   */
  public static Verifier checkAll(Verifier ... verifiers) {
    return (op, form, arg) -> {
      
      String mess = null;
      
      System.out.println("---checkall!!!!");
      
      for (Verifier v : verifiers) {
        try {
          v.verify(op, form, arg);
          System.out.println("passed!!!");
        } catch (VerificationException e) {
          mess = mess == null ? e.getMessage() + System.lineSeparator() :
                                mess + e.getMessage() + System.lineSeparator();
        }      
      }
      
      System.out.println("        --> "+mess);
      
      if (mess != null) {
        throw new VerificationException(mess);
      }
    };
  }
  
  /**
   * Returns a single Verifier that 
   * iterates over the provided Verifiers and returns at the first success
   * @param verifiers - the Verifiers to invoke
   * @return a Verifier that returns at the first success
   */
  public static Verifier any(Verifier ... verifiers) {
    return (op, form, arg) -> {
      for (Verifier v : verifiers) {
        v.verify(op, form, arg);
        return;
      }
    };
  }
}
