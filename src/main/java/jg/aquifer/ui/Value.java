package jg.aquifer.ui;

/**
 * Wrapper class for an argument to an Option
 * @author Jose Guaro
 */
public class Value {
  
  private String value;
  private boolean verified;
  
  public Value() {
    this.verified = false;
  }
  
  public Value setValue(String value) {
    this.value = value;
    return this;
  }
  
  public String getValue() {
    return value;
  }
  
  public boolean isVerified() {
    return verified;
  }
  
  public Value verify() {
    this.verified = true;
    return this;
  }
  
  public Value unverify() {
    this.verified = false;
    return this;
  }
  
  @Override
  public String toString() {
    return "|"+value+"|  isVerified? "+verified;
  }
}
