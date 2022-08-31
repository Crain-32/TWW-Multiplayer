package client.communication;

import constants.WorldType;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class GameRoomConfig {

    private String gameRoomName;
    private String password;
    private String playerName;
    private Integer worldId;
    private Integer playerAmount;
    private Integer worldAmount;
    private WorldType worldType;
    private WorldType roomType;

}
