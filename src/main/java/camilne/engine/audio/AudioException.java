package camilne.engine.audio;

public class AudioException extends Exception {

    public AudioException(String message) {
        super(message);
    }

    public AudioException(Exception e) {
        super(e);
    }

}
