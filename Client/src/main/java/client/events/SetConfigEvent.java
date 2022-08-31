package client.events;

import constants.WorldType;
import lombok.Data;

@Data
public class SetConfigEvent {
    private String gameRoomName;
    private String password;
    private String playerName;
    private Integer worldId;
    private Integer playerAmount;
    private Integer worldAmount;
    private WorldType worldType;
    private WorldType roomType;
}
