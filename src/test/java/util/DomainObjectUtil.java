package util;

import crain.model.constants.WorldType;
import crain.model.domain.GameRoom;
import crain.model.domain.Player;

public class DomainObjectUtil {

    public static Player createTestPlayer(String playerName, WorldType worldType, Integer worldId) {
        return Player.builder().playerName(playerName).worldType(worldType).worldId(worldId).gameRoom(new GameRoom()).build();
    }

}
