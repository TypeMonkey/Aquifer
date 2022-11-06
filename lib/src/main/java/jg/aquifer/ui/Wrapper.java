package jg.aquifer.ui;

/**
 * A utility class that wraps a value
 * 
 * This can be helpful when creating custom Options 
 * with multiple Ui nodes that need to communicate each other
 * is a simple manner 
 * (like checking if a user pressed a TextField for the first time, etc.)
 */
public class Wrapper<T> {
  
  private volatile T value;

  public Wrapper(T value) {
    this.value = value;
  }

  public void set(T value) {
      this.value = value;
  }

  public T get() {
      return value;
  }
}
