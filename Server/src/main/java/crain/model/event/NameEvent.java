package crain.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import records.ROOM;

@Data
@AllArgsConstructor
public class NameEvent {

    private ROOM.PlayerRecord playerRecord;
    private String gameRoom;
}
