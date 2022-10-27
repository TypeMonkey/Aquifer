package jg.aquifer.ui.io;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * A java.io.Writer that writes to a TextArea.
 */
public class UIPrinter extends Writer {

    private final TextArea targetArea;

    public UIPrinter(TextArea targetArea) {
        this.targetArea = targetArea;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        char [] range = Arrays.copyOfRange(cbuf, off, off + len);        
        Platform.runLater(() -> {targetArea.appendText(String.valueOf(range));});
    }

    @Override
    public void flush() throws IOException {}

    @Override
    public void close() throws IOException {}
    
}
