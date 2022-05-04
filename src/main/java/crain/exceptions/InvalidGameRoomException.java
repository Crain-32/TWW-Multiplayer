package crain.exceptions;

public class InvalidGameRoomException extends RuntimeException {
    private final String GAMEROOM_NAME;

    public InvalidGameRoomException(String message) {
        super(message);
        this.GAMEROOM_NAME = "";
    }

    public InvalidGameRoomException(String message, String gameRoom) {
        super(message);
        this.GAMEROOM_NAME = gameRoom;
    }

    public String getGameRoomName() {
        return this.GAMEROOM_NAME;
    }
}
