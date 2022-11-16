package jg.sample;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jg.aquifer.Intake;
import jg.aquifer.commands.Program;
import jg.aquifer.commands.Subcommand;
import jg.aquifer.commands.Verifier;
import jg.aquifer.commands.options.ExclusiveOptions;
import jg.aquifer.commands.options.FileOption;
import jg.aquifer.commands.options.Flag;
import jg.aquifer.commands.options.Option;
import jg.aquifer.commands.options.RadioOption;
import jg.aquifer.ui.Editor;
import jg.aquifer.ui.Visualizer;
import jg.aquifer.ui.form.FormConstants;
import jg.aquifer.ui.form.FormPane;
import jg.aquifer.ui.io.IO;
import jg.aquifer.ui.output.OutputPane;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Program program = new Program("sampleProg", "This is a sample program to test things out");

        for(int i = 1; i < 4; i++) {
            program.addSubcommand(new Subcommand("other"+i));
        }
        
        Subcommand subcommand = new Subcommand("withdesc");
        subcommand.setDescription("This is a subcommand for the program");  
        subcommand.addOption(new Option("minus", "Removes a specific file", true, Verifier.WHOLE_NUM));
        
        program.addSubcommand(subcommand);

        program.setIcon(new Image(Main.class.getClassLoader().getResourceAsStream("sample_icon.png")));
        
        Option [] exs = {
          new Option("ex1", "some exclusive option 1"),
          new Option("ex2", "some exclusive option 2"), 
          new RadioOption("ex3", "some exclusive option 2", false, "choice1", "choice2", "choice3"),
          new Flag("Enable Assertions", "Whether to enforce assertions"),
          new Flag("Enable Disertions", "Whether to enforce assertions")
        };

        ExclusiveOptions exclusiveOptions = new ExclusiveOptions("SampExclusive", 
                                                                "Set of exclusive options", 
                                                                Arrays.asList(exs), 
                                                                true);
        program.addOption(exclusiveOptions);

        program.addOption(new Option("add", "Adds files in the current directory"));
        program.addOption(new Option("commit", "Commits files in the current directory"));
        program.addOption(new Option("push", "Pushes files in the current directory"));
        
        program.addOption(new Option("amend", "Amends files in the current directory", true));
        program.addOption(new RadioOption("multiradio", "Multiple files in the current directory", true, "option1", "option2", "option3"));
        //program.addProgramOption(new FileOption("multi", "Multiple files in the current directory", true, Verifier.STR_VERIFIER, new File(System.getProperty("user.home"))));
        
        program.addOption(new Flag("allowDups", "Allows duplicate files and something else"));
        program.addOption(new Flag("flag2", "2Allows duplicate files"));
        program.addOption(new Flag("flag3", "3Allows duplicate files"));
        program.addOption(new Flag("flag4", "4Allows duplicate files"));


        Visualizer generator = new Visualizer(program, new Intake() {
            @Override
            public void submitArguments(String subcommand, Map<String, String> arguments, IO printer) {
                new Thread(() -> {
                printer.out.println("  --> Argument: "+subcommand+" | "+arguments);

                Scanner scanner = new Scanner(printer.in);
                printer.out.println("---- I'll be echoing you!");

                while (scanner.hasNext()) {
                    printer.out.println(scanner.nextLine());
                }

                scanner.close();
                System.out.println("----  DONE!!!");

                /*
                for(int i = 0; i < 100 ; i++) {
                    try {
                    printer.out.println("  --> This is "+i+"th message I'm printing!!!");
                    Thread.sleep(150);
                    } catch (Exception e) {
                    printer.out.println("Exception!! "+e.getMessage());
                    }
                }
                
                Exception exception = new Exception("dummy error!");
                exception.printStackTrace(printer.err);
                */
                
                }).start();
            }
        });        

        Editor editor = new Editor() {

          @Override
          public void edit(FormPane formPane, OutputPane outputPane) {
            System.out.println("STATUS "+(FormConstants.HEADER_PANE_FUNC.apply(formPane).getId().equals(FormConstants.HEADER_PANE)));
          }
          
        };

        generator.initialize(editor);

        generator.show();
        
        /*
        final Options options = new Options();
        options.addOption(Option.builder("sample").type(int.class).required().build());
        
        DefaultParser parser = new DefaultParser();
        CommandLine cl = parser.parse(options, "-sample Albert".split("\\s+"));
        */
        
        //TypeHandler.createValue("15", Integer.class);
    }
    
    public static void main(String[] args) {
        System.out.println(System.getProperty( "javafx.runtime.version" ));
        Main.launch(args);
    }
}
