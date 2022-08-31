package client.exceptions;

public class GameHandlerDisconnectException extends RuntimeException {

    public GameHandlerDisconnectException(String message) {
        super(message);
    }
}
