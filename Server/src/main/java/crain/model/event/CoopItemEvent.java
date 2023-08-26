package crain.model.event;

import records.INFO;


public record CoopItemEvent(INFO.CoopItemRecord itemRecord, String gameRoom) {
}
