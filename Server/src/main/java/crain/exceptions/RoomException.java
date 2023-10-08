package crain.exceptions;

public class RoomException extends RuntimeException {
    private final String GAMEROOM_NAME;

    public RoomException(String message) {
        super(message);
        this.GAMEROOM_NAME = "";
    }

    public RoomException(String message, String gameRoom) {
        super(message);
        this.GAMEROOM_NAME = gameRoom;
    }

    public String getGameRoomName() {
        return this.GAMEROOM_NAME;
    }
}
