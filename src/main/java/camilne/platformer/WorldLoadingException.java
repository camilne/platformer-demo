package camilne.platformer;

public class WorldLoadingException extends Exception {

    public WorldLoadingException(String message) {
        super(message);
    }

    public WorldLoadingException(Exception e) {
        super(e);
    }

}
