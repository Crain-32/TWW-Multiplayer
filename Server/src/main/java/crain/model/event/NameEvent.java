package crain.model.event;

import records.ROOM;

public record NameEvent(
        String gameRoom,
        ROOM.PlayerRecord playerRecord
        ) implements MultiplayerEvent<ROOM.PlayerRecord> {
    @Override
    public String getGameRoomName() {
        return gameRoom;
    }

    @Override
    public ROOM.PlayerRecord getPayload() {
        return playerRecord;
    }
}
