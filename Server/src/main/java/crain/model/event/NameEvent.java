package crain.model.event;

import records.ROOM;

public record NameEvent(ROOM.PlayerRecord playerRecord, String gameRoom) {
}
