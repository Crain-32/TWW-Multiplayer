package crain.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import records.INFO;
@Data
@AllArgsConstructor
public class MultiworldItemEvent {

    private INFO.ItemRecord itemRecord;
    private String gameRoom;
}
