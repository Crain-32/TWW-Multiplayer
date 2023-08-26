package crain.model.event;

import records.INFO;

public record MultiworldItemEvent(INFO.ItemRecord itemRecord, String gameRoom) {

}
