package crain.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameRoomMessageEvent {

    private String message;
    private String gameRoom;
}
