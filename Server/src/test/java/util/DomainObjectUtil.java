package util;

import constants.WorldType;
import crain.model.domain.GameRoom;
import crain.model.domain.Player;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DomainObjectUtil {
    public static Player createTestPlayer(String playerName, WorldType worldType, Integer worldId) {
        return createTestPlayer(playerName, worldType, worldId, new GameRoom());
    }

    public static Player createTestPlayer(String playerName, WorldType worldType, Integer worldId, GameRoom gameRoom) {
        return Player.builder().playerName(playerName).worldType(worldType).worldId(worldId).gameRoom(gameRoom).build();
    }

}
