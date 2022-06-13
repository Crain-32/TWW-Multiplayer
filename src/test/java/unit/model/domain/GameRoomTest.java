package unit.model.domain;

import crain.exceptions.InvalidPlayerException;
import crain.model.constants.WorldType;
import crain.model.domain.GameRoom;
import crain.model.domain.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.DomainObjectUtil;

import java.util.ArrayList;

@DisplayName("GameRoom Unit Test")
public class GameRoomTest {

    @Test
    public void addPlayerToRoomTest() {
        GameRoom gameRoom = GameRoom.builder().worldAmount(3).players(new ArrayList<>()).build();
        Player player = DomainObjectUtil.createTestPlayer("Player One", WorldType.MULTIWORLD, 1);
        Player playerTwo = DomainObjectUtil.createTestPlayer("Player Two", WorldType.MULTIWORLD, 2);
        Player playerThree = DomainObjectUtil.createTestPlayer("Player Three", WorldType.SHARED, 2);
        Player playerFour = DomainObjectUtil.createTestPlayer("Player Four", WorldType.COOP, 4);

        gameRoom.addPlayer(player);
        Assertions.assertEquals(gameRoom, player.getGameRoom());

        Assertions.assertDoesNotThrow(() -> gameRoom.addPlayer(playerTwo));
        Assertions.assertEquals(player.getGameRoom(), playerTwo.getGameRoom());

        playerTwo.setWorldType(WorldType.SHARED);
        Assertions.assertThrows(InvalidPlayerException.class, () -> gameRoom.addPlayer(playerTwo));
        Assertions.assertDoesNotThrow(() -> gameRoom.addPlayer(playerThree));
        Assertions.assertThrows(InvalidPlayerException.class, () -> gameRoom.addPlayer(playerFour));
    }

}
