package crain.exceptions;

public class RoomException extends RuntimeException {
    private final String gameRoomName;

    public RoomException(String message) {
        super(message);
        this.gameRoomName = "";
    }

    public RoomException(String message, String gameRoom) {
        super(message);
        this.gameRoomName = gameRoom;
    }

    public String getGameRoomName() {
        return this.gameRoomName;
    }
}
