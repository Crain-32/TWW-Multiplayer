package crain.exceptions;

public class InvalidGameRoomException extends RoomException {

    public InvalidGameRoomException(String message) {
        super(message);
    }

    public InvalidGameRoomException(String message, String gameRoom) {
        super(message, gameRoom);
    }
}
