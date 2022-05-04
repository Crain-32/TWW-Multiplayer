package crain.exceptions;

public class InvalidPlayerException extends Exception{
    private final String GAMEROOM_NAME;

    public InvalidPlayerException(String message) {
        super(message);
        this.GAMEROOM_NAME = "";
    }
    public InvalidPlayerException(String message, String gameRoom) {
        super(message);
        this.GAMEROOM_NAME = gameRoom;
    }

    public String getGameRoomName() {
        return this.GAMEROOM_NAME;
    }
}
