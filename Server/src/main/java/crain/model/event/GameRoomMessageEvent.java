package crain.model.event;

import records.ROOM;

public record GameRoomMessageEvent(
        String message,
        String gameRoom
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
