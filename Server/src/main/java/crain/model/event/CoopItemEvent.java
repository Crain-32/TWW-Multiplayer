package crain.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import records.INFO;

@Data
@AllArgsConstructor
public class CoopItemEvent {

    private INFO.CoopItemRecord itemRecord;
    private String gameRoom;

}
