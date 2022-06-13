package crain.exceptions;

public class InvalidPlayerException extends RoomException {
    public InvalidPlayerException(String message) {
        super(message);
    }
    public InvalidPlayerException(String message, String gameRoom) {
        super(message, gameRoom);
    }
}
