# Aquifer
Quickly generate graphical user interfaces (GUI) for your terminal applications without fussing with layouts and design! 
**Aquifer** is written purely in Java, along with JavaFX.

# Requirements
- At least JDK 8 with JavaFX 8. 
	- JRE 1.8 can also be used, but it needs JavaFX 8 bundled with it.

_**Why are you using JavaFX 8?**_ It seems JDK 8 is still the de facto standard for most development setups
and Oracle JDK 8 (which is the one used to develop this library) bundles JavaFX 8 with it. That said, I
have no doubt that Aquifer can be easily ported to JDK 9+ and JavaFX 9+.

## Usage

### Option
We first start with the `Option` class. An `Option` describes a parameter for your program (or a subprogram/subcommand of it). 
```java
Option requiredOption = new Option("req", "This is a required option", true);
```
 This creates a required Option with the name  `req` whose description is `This is a required option`. Passing `false` instead would make this Option optional.

Additionally, we can create the equivalent Option using `Option.create` by: 
```java
Option requiredOption = Option.create("req", "This is a required option").require().build();
```
Now that we have an `Option`, we can either set it to be tied to your program or a subcommand of your program.

### Program
A `Program` represents the overall `Options` of your application.

We can add our `Option` to our `Program` by doing:
```java
// Creates a Program
Program program = new Program("Example Program", "This is a description");

// Adds the Option we created above as a paramter (that's required) for our program
program.addProgramOption(requiredOption);
```
### Subcommand
There may be situations where we prefer a separation of responsibilities in our application. With Aquifer, you can use `Subcommands` to group a set of `Options` tied to a specific part of your application.

This is synonymous with the `commit`, `add`, `push`, etc. subcommands of Git.

To do so:
```java
// Creates a subcommand called 'subOne'
Subcommand subcommand = new Subcommand("subOne");

// Sets a description for this subcommand
subcommand.setDescription("This is a description for this subcommand");

// Adds the Option we created above to the 'subOne' subcommand
subcommand.addOption(requiredOption);
```
The `Options` of a `Subcommand` must be unique - in terms of it name. A `Program` itself is represented with its own `Subcommand` instance - which is how we're able to add program parameters.

We then add our `Subcommand` to our `Program` by doing:
```java
program.addSubcommand(subcommand);
```

In addition, a `Program` must be composed of unique `Subcommands` (by their name) if any exists. Which means it's **not possible** to create a `Subcommand` with the same name as your program.

### Intake
Now that we have our `Program` created, we can now visualize it! 

But before we do, we need to implement the interface `jg.aquifer.Intake`, which is the drop off point for arguments to `Options` that was submitted by the user and entry point of your application.

Here's an example implementation
```java
public class ProgramIntake implements Intake {

  @Override
  public void submitArguments(String subcommand, Map<String, String> args, IO io) {
    if (subcommand.equals("subOne")) {
      // We retreive the inputted argument from the mapping
      String argument = args.get("req");
      
      // We can also print output to the GUI using printer.
      io.out.println("You gave me the argument '"+argument+"'");
    }
  }
}
```

### Visualizer
Okay, I wanna see this GUI now! We can do so by doing:
```java
//We need to pass our Program and Intake instances.
Visualizer generator = new Visualizer(program, new ProgramIntake());

//This method must be called prior to show()
generator.initialize();

//Displays the GUI. Does not block.
generator.show();
```
Giving us the following GUI:

![enter image description here](https://i.imgur.com/Z5bbu0d.png)

## Verification
Sometimes, it's useful to verify arguments prior to being submitted to your application. With Aquifer, you can easily do so while providing live feedback to the user.

Recall our `Options` instance from above:
```java
Option requiredOption = Option.create("req", "This is a required option").require().build();
```
Say, we've decided to make the argument to `req` to only be non-negative integers. To create such a requirement for Aquifer to enforce, we'll need to pass our `Option` a `Verifier`:

```java
Verifier noNegatives = (op, form, arg) -> {
    try {
      if(Long.parseLong(arg) < 0) {
        throw new VerificationException("Expected a non-negative whole number");
      }
    } catch (NumberFormatException e) {
      throw new VerificationException("Expected a non-negative whole number");
    }
  };


Option requiredOption = Option.create("req", "This is a required option")
                              .require()
                              .setVerifier(noNegatives)
                              .build();
```

By throwing a `VerificationException`, we're indicating to Aquifer that the supplied argument is invalid. The message passed to the `VerificationException` is then printed on the GUI. 

![enter image description here](https://i.imgur.com/j73mcj3.png)

## Icons
Flashy GUIs are the best! You can set for a small program icon to be display on the header by doing:
```java
//Using javafx.scene.image.Image
Image appIcon = new new Image(new FileInputStream(new File("sample_icon.png")));

//Set the program's icon. 
//Note: This should be called before using Visualizer
program.setIcon(appIcon);
```

which gives us something similar to:

![](https://i.imgur.com/eMJqzBZ.png)

## Option Types
While the base `Option` class maybe sufficient for most needs, Aquifer comes with a few other *options*.

### Flag
A `Flag` is an `Option` that has **no arguments** and **isn't required**. They are displayed as selectable choices under the "*Flags*" section of the Subcommand it belongs to.

Below, we re-create the `ls` terminal command, using `Flags` to declare the command's many flags.
```java
Program program = new Program("ls", "Lists contents of a directory");

program.addOption(new Flag("all", "do not ignore entries starting with ."));
program.addOption(new Flag("recursive", "list subdirectories recursively"));
program.addOption(new Flag("size", "print the allocated size of each file, in blocks"));
```

which results in:
![](https://i.imgur.com/ZBwdFE4.png)

### RadioOption
A `RadioOption` is an `Option` whose argument can only be **one out of a specified set of possible values**. Visually, it's synonymous with the idea of ["radio buttons"](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/radio) - a group of buttons which only one - or none - can be selected at a time.

`RadioOptions` are either required or not, and are displayed similarly to a base `Option` but with the text field replaced with a group of radio buttons labeled with the choices provided.

Below, we use a `RadioOption` - with each possible value representing a supported Garbage Collection mode for the Java Virtual Machine. Only one mode can be chosen at a time.
```java
Program program = new Program("jrunner", "Runs an executable JAR with the given JVM arguments");

RadioOption gcModes = new RadioOption("Garbage Collection Mode",
                                      "Sets the Garbage collection mode of the running JVM",
                                      false,            //whether this is a required Option
                                      "Mark and Sweep", //option 1
                                      "G1",             //option 2
                                      "Serial",         //option 3
                                      "Parallel");      //option 4
program.addOption(gcModes);
```

which results in:
![](https://i.imgur.com/oYpUXrw.png)

### FileOption
A `FileOption` is a variant of the regular `Option` in that it allows users to choose a file and/or directory using the user's OS visual file viewer.

Below, we extend the example from above and add a required `FileOption` that allows users to visually pick a `.jar` file from their file system.

```java
ExtensionFilter jarFiler = new ExtensionFilter("JAR File", "*.jar", "*.JAR");
FileOption jarOption = new FileOption("Target JAR File", 
                                      "The JAR file to run", 
                                      true, 
                                      jarFiler);
program.addOption(jarOption);
```

which results in:
![](https://i.imgur.com/yucALxT.png)
Pressing __Browse...__ opens the user's file viewer.

### ExclusiveOptions
An `ExclusiveOptions` is formed from a collection of unique `Option`, and only at most one of these options can be chosen to be an argument to the host `ExclusiveOptions`.

Below, we have a variant of Git's `commit` message option. Our variant allows for a commit message to be from a file (`FileOption`), be a one-liner (`Option`) or be chosen from a set of short phrases.

```java
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

//Note: it only matters that whether ExclusiveOptions is required or not
//      , ignoring the required status of its child Options

program.addOption(exclusiveOptions);
```

which results in:
![](https://i.imgur.com/yU6kAG4.png)
Only one of the three `Options` can be selected.

### Making your own Option type
If there's use case that the bundled `Option` types doesn't quite answer, the `Option` class can be readily extended for customization. 
The `generateDisplay()` is the method that houses the display logic for an `Option`. Override this method with the logic for visually displaying your implementation and you should be set!

## Download
### Maven
```xml
<dependency>
  <groupId>io.github.typemonkey</groupId>
  <artifactId>aquifer</artifactId>
  <version>2.0.0</version>
</dependency>
```

### Gradle
`implementation 'io.github.typemonkey:aquifer:2.0.0-SNAPSHOT`

### Gradle (Kotlin)
`implementation("io.github.typemonkey:aquifer:2.0.0-SNAPSHOT")`

Other build tools can be used as shown [here](https://central.sonatype.dev/artifact/io.github.typemonkey/aquifer/1.0.0).

## Roadmap
There's a lot I want to do with Aquifer. Here's what I currently have planned:
- Bindings for various CLI Libraries (Commons CLI and PicoCLI are my priority)
- ~~Publish to Central Maven Repository~~ ( ___Done!___ )
- New bundled Option types (numerical sliders, mutually exclusive options, etc)
  - ___In 2.0:___ Mutually Exclusive options added
- Better console output support (allowing user input)
  - ___in 2.0:___ Seperate input area for user input  
- Design improvements for the generated UI (Listener interface(s) for intake program - can check when user has exited/closed UI, etc.)
  - ___in 2.0:___ Arguments to specific options are not observable. `getStage()` added to `Visualizer` so window listeners can be added easily.   
- Create a Contribution Guide 

If you have thoughts or opinions on how I could go about any of these goals, I'm all ears. Open up an issue or [email me](mailto:anothertypemonkey@gmail.com) describing what you have in mind and lets get working!

## Acknowledgements
This project would not be possible without [Gooey](https://github.com/chriskiehl/Gooey). 

I've been a user of the library and as a person that doesn't have a bone for graphical design, it was too convenient to let go of when creating terminal-based Python applications. It's a highly capable library.
