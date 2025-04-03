package crain.model.event;

import records.INFO;

public record EventEvent(
        String gameRoom,
        INFO.EventRecord eventRecord
        ) implements MultiplayerEvent<INFO.EventRecord> {
    @Override
    public String getGameRoomName() {
        return gameRoom;
    }

    @Override
    public INFO.EventRecord getPayload() {
        return eventRecord;
    }
}

