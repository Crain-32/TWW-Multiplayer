package crain.model.event;

import records.INFO;

public record EventEvent(
        INFO.EventRecord eventRecord,
        String gameRoom
) {
}

