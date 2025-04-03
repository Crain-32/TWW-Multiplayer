package crain.model.event;

import records.INFO;

public record MultiworldItemEvent(
        String gameRoom,
        INFO.ItemRecord itemRecord
) implements MultiplayerEvent<INFO.ItemRecord> {
    @Override
    public String getGameRoomName() {
        return gameRoom;
    }

    @Override
    public INFO.ItemRecord getPayload() {
        return itemRecord;
    }
}
