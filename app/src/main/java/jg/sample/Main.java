package jg.sample;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jg.aquifer.Intake;
import jg.aquifer.commands.Program;
import jg.aquifer.commands.Subcommand;
import jg.aquifer.commands.Verifier;
import jg.aquifer.commands.options.FileOption;
import jg.aquifer.commands.options.Flag;
import jg.aquifer.commands.options.Option;
import jg.aquifer.commands.options.RadioOption;
import jg.aquifer.ui.Visualizer;
import jg.aquifer.ui.io.IO;

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
        
        program.addProgramOption(new Option("add", "Adds files in the current directory"));
        program.addProgramOption(new Option("commit", "Commits files in the current directory"));
        program.addProgramOption(new Option("push", "Pushes files in the current directory"));
        
        program.addProgramOption(new Option("amend", "Amends files in the current directory", true));
        program.addProgramOption(new RadioOption("multiradio", "Multiple files in the current directory", true, "option1", "option2", "option3"));
        //program.addProgramOption(new FileOption("multi", "Multiple files in the current directory", true, Verifier.STR_VERIFIER, new File(System.getProperty("user.home"))));
        
        program.addProgramOption(new Flag("allowDups", "Allows duplicate files"));
        program.addProgramOption(new Flag("flag2", "2Allows duplicate files"));
        program.addProgramOption(new Flag("flag3", "3Allows duplicate files"));
        program.addProgramOption(new Flag("flag4", "4Allows duplicate files"));


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
        
        generator.initialize();
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
