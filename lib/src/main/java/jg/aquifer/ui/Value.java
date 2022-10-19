package jg.aquifer.ui;

/**
 * Wrapper class for an argument to an Option
 * 
 * @author Jose
 */
public class Value {
  
  private String value;
  private boolean verified;
  
  /**
   * Constructs a Value that is initially unverified
   */
  public Value() {
    this.verified = false;
  }
  
  /**
   * Sets the String value
   * @param value - the String value
   * @return the current Value instance
   */
  public Value setValue(String value) {
    this.value = value;
    return this;
  }
  
  /**
   * Returns the String value
   * @return the String value
   */
  public String getValue() {
    return value;
  }
  
  /**
   * Returns the verification status of the backing String value
   * @return true if the associated Verifier with this Value's option
   *         has successfully verified the String value, false if else.
   */
  public boolean isVerified() {
    return verified;
  }
  
  /**
   * Sets the verification status of this Value to be true. 
   * Intended to be called by the Verifier that's associated with this Value's Option.
   * @return the current Value instance
   */
  public Value verify() {
    this.verified = true;
    return this;
  }
  
  /**
   * Sets the verification status of this Value to be false. 
   * Intended to be called by the Verifier that's associated with this Value's Option.
   * @return the current Value instance
   */
  public Value unverify() {
    this.verified = false;
    return this;
  }
  
  @Override
  public String toString() {
    return "|"+value+"|  isVerified? "+verified;
  }
}
