package crain.model.event;

import records.ROOM;

public record ErrorEvent(
        String gameRoom,
        String message
) implements MultiplayerEvent<ROOM.MessageRecord> {
    @Override
    public String getGameRoomName() {
        return gameRoom;
    }

    @Override
    public ROOM.MessageRecord getPayload() {
        return new ROOM.MessageRecord(message());
    }
}
