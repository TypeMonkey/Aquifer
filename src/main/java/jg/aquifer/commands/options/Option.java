package jg.aquifer.commands.options;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jg.aquifer.commands.Subcommand;
import jg.aquifer.commands.Verifier;
import jg.aquifer.ui.RawArgumentForm;
import jg.aquifer.ui.Value;

/**
 * An option is a named parameter to a Subcommand, which may or may not be required.
 * 
 * Visually, an Option takes in user input (as a string) as its argument. That argument
 * is then passed to the Option's Verifier. If the Verifier throws a VerificationException, 
 * the exception's message is displayed.
 * 
 * @author Jose
 */
public class Option {  
  
  /**
   * Convenient builder class for a regular Option.
   * @author Jose
   */
  public static class Builder {
    private String optName;
    private String description;
    private boolean isRequired;
    private Verifier verifier;
    
    protected Builder(String optName) {
      this.optName = optName;
      this.description = "";
      this.verifier = Verifier.STR_VERIFIER;
    }
    
    public Builder setOptName(String optName) {
      this.optName = optName;
      return this;
    }
    
    public Builder setDescription(String description) {
      this.description = description;
      return this;
    }
    
    public Builder require() {
      this.isRequired = true;
      return this;
    }
    
    public Builder optional() {
      this.isRequired = false;
      return this;
    }
    
    public Builder setVerifier(Verifier verifier) {
      this.verifier = verifier;
      return this;
    }
    
    public Option build() {
      return new Option(optName, description, isRequired, verifier);
    }
  }
  
  private final String optName;
  private final String description;
  private final boolean isRequired;
  private final Value holder;
  private final Verifier verifier;
  
  private Node display;
  
  /**
   * Constructs an optional Option with a passive Verifier (Verifier.STR_VERIFIER)
   * @param optName - the name of this Option
   * @param description - the description of this Option
   */
  public Option(String optName, String description) {
    this(optName, description, false, Verifier.STR_VERIFIER);
  }
  
  /**
   * Constructs an Option with a passive Verifier (Verifier.STR_VERIFIER)
   * @param optName - the name of this Option
   * @param description - the description of this Option
   * @param isRequired - whether this Option is required
   */
  public Option(String optName, String description, boolean isRequired) {
    this(optName, description, isRequired, Verifier.STR_VERIFIER);
  }

  /**
   * Constructs an Option with a custom Verifier
   * @param optName - the name of this Option
   * @param description - the description of this Option
   * @param isRequired - whether this Option is required
   * @param verifier - the Verifier to use to validate arguments to this Option
   */
  public Option(String optName, String description, boolean isRequired, Verifier verifier) {
    this.optName = optName;
    this.description = description;   
    this.isRequired = isRequired;
    this.verifier = verifier;
    this.holder = new Value();
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Option) {
      return ((Option) obj).optName.equals(optName);
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return optName.hashCode();
  }
  
  public final Node makeDisplay(RawArgumentForm argumentForm, Subcommand subcommand) {
    if (display == null) {
      display = generateDisplay(argumentForm, subcommand);
    }
    
    return display;
  }
  
  /**
   * Generates the graphics to display for this VerifiableOption on the generated UI
   * @param argumentForm - the RawArgumentForm that the inputed argument will be applied to
   * @param subcommand - the Subcommand this VerifiableOption belongs to.
   * @return the Node that holds the graphics for this VerifiableOption
   */
  protected Node generateDisplay(RawArgumentForm argumentForm, Subcommand subcommand) {    
    final VBox mainCellLayout = new VBox(5);
    
    final Text argumentName = new Text(optName);
    argumentName.setFont(Font.font("System Regular", FontWeight.BOLD, 14));
    
    final Text argumentDescription = new Text(description);
    argumentDescription.setFont(Font.font("System Regular", FontPosture.ITALIC, 14));
    
    final TextFlow flow = new TextFlow(argumentName, new Text(System.lineSeparator()), argumentDescription);
    mainCellLayout.getChildren().add(flow);
    
    final HBox entryCellHBox = new HBox(5);
    final TextField argEntry = new TextField("Enter value");
    
    argEntry.setOnMouseClicked(new EventHandler<Event>() {
      private boolean justInitialized = true;
      
      @Override
      public void handle(Event event) {
        if (justInitialized) {
          argEntry.setText("");
          justInitialized = false;
        }
      }
    });
    
    final Label exceptionLabel = new Label();
    exceptionLabel.setTextFill(Color.RED);
    
    argEntry.textProperty().addListener((observable, oldValue, newValue) -> { 
      argumentForm.setOptionArgument(this, holder);
      
      if (!newValue.isEmpty()) {
        try {
          verifier.verify(this, argumentForm, newValue);
          exceptionLabel.setText("");
          holder.setValue(newValue).verify();
          mainCellLayout.getChildren().remove(exceptionLabel);
        } catch (VerificationException e) {
          if(!mainCellLayout.getChildren().contains(exceptionLabel)) {
            exceptionLabel.setText(e.getMessage());
            mainCellLayout.getChildren().add(exceptionLabel);
          }
          
          holder.unverify();
        }
      }
    });
    
    entryCellHBox.getChildren().add(argEntry);
    
    
    mainCellLayout.getChildren().add(entryCellHBox);
    //mainCellLayout.getChildren().add(exceptionLabel);
    
    return mainCellLayout;
  }
  
  public Value getHolder() {
    return holder;
  }
  
  public String getOptName() {
    return optName;
  }
  
  public String getDescription() {
    return description;
  }
  
  public boolean isRequired() {
    return isRequired;
  }
  
  public Verifier getVerifier() {
    return verifier;
  }
  
  @Override
  public String toString() {
    return optName+" : "+description;
  }
  
  public static Builder create(String name, String desc) {
    return new Builder(name).setDescription(desc);
  }
}
