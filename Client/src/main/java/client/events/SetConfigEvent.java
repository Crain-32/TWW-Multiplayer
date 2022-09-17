package client.events;

import constants.WorldType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
