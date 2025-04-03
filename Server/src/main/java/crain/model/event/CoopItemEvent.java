package crain.model.event;

import records.INFO;


public record CoopItemEvent(
        String gameRoom,
        INFO.CoopItemRecord itemRecord
) implements MultiplayerEvent<INFO.CoopItemRecord> {
    @Override
    public String getGameRoomName() {
        return gameRoom;
    }

    @Override
    public INFO.CoopItemRecord getPayload() {
        return itemRecord;
    }
}
