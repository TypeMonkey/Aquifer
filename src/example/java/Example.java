package example;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jg.aquifer.Intake;
import jg.aquifer.Printer;
import jg.aquifer.commands.Program;
import jg.aquifer.commands.Subcommand;
import jg.aquifer.commands.Verifier;
import jg.aquifer.commands.options.Option;
import jg.aquifer.ui.Visualizer;

public class Example extends Application {
  
  public static class ProgramIntake implements Intake {

    @Override
    public void submitArguments(String subcommand, Map<String, String> args, Printer printer) {
      if (subcommand.equals("subOne")) {
        // We retreive the inputted argument from the mapping
        String argument = args.get("req");
        
        // We can also print output to the GUI using printer.
        printer.println("You gave me the argument '"+argument+"'");
      }
    }

  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Option requiredOption = Option.create("req", "This is a required option").require().setVerifier(Verifier.NON_NEG_WHOLE).build();
    
    Program program = new Program("sampleProg", "This is a sample program to test things out");
    program.setIcon(new Image(new FileInputStream(new File("sample_icon.png"))));

    
    Subcommand subcommand = new Subcommand("subOne");
    subcommand.addOption(requiredOption );
    
    program.addSubcommand(subcommand);
    
    Visualizer visualizer = new Visualizer(program, new ProgramIntake());
    visualizer.initialize();
    visualizer.show();
  }

  public static void main(String[] args) throws Exception{
    System.out.println("---"+System.getProperty( "javafx.runtime.version" ));
    Example.launch(args);
  }

}

