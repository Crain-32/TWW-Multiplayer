package crain.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import records.INFO;

@Data
@AllArgsConstructor
public class EventEvent {

    private INFO.EventRecord eventRecord;
    private String gameRoom;
}
