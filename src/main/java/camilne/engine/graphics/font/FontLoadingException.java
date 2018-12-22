package camilne.engine.graphics.font;

public class FontLoadingException extends Exception {

    public FontLoadingException(String message) {
        super(message);
    }

    public FontLoadingException(Exception e) {
        super(e);
    }

}
