package jg.sample;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import jg.aquifer.Intake;
import jg.aquifer.commands.Program;
import jg.aquifer.commands.Verifier;
import jg.aquifer.commands.options.ExclusiveOptions;
import jg.aquifer.commands.options.FileOption;
import jg.aquifer.commands.options.Flag;
import jg.aquifer.commands.options.Option;
import jg.aquifer.commands.options.RadioOption;
import jg.aquifer.ui.Visualizer;
import jg.aquifer.ui.io.IO;

public class RMExamples extends Application {
  
  public static void main(String [] args) throws Exception {
    RMExamples.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    /*
    Program program = new Program("ls", "Lists contents of a directory");

    program.addOption(new Flag("all", "do not ignore entries starting with ."));
    program.addOption(new Flag("recursive", "list subdirectories recursively"));
    program.addOption(new Flag("size", "print the allocated size of each file, in blocks"));

    */

    /*
    Program program = new Program("jrunner", "Runs an executable JAR with the given JVM arguments");
    RadioOption gcModes = new RadioOption("Garbage Collection Mode", 
                                          "Sets the Garbage collection mode of the running JVM", 
                                          false, 
                                          "Mark and Sweep", 
                                          "G1", 
                                          "Serial", 
                                          "Parallel");
    program.addOption(gcModes);
    program.addOption(new Flag("Enable Assertions", "Whether to enforce assertions"));

    ExtensionFilter jarFiler = new ExtensionFilter("JAR File", "*.jar", "*.JAR");
    FileOption jarOption = new FileOption("Target JAR File", 
                                          "The JAR file to run", 
                                          true, 
                                          jarFiler);
    program.addOption(jarOption);
    */

    Program program = new Program("Quick Git", "Collection of Git shortcuts");
    RadioOption quickMs = new RadioOption("Quick Git Message", 
                                          "Collection of quick commit messages", 
                                          false, 
                                          "Minor bug fixes", 
                                          "Major fixes. See file changes...", 
                                          "I messed up my line endings. This will hopefully fix it,");

    FileOption commitFile = new FileOption("Commit File", 
                                          "Choose a file as your commit message", 
                                          true);

    Option commitEntry = new Option("Enter Commit Message", "Enter a one-line commit message", true);

    ExclusiveOptions exclusiveOptions = new ExclusiveOptions("Commit Message", 
                                                             "The message to attach to this commit", 
                                                             Arrays.asList(quickMs, commitFile, commitEntry), true);

    program.addOption(exclusiveOptions);

    Visualizer generator = new Visualizer(program, new Intake() {
      @Override
      public void submitArguments(String subcommand, Map<String, String> arguments, IO streams) {
        streams.out.println(arguments);
      }  
    });        
    generator.initialize();
    generator.show();
  }

}
