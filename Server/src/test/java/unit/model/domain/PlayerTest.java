package unit.model.domain;

import constants.WorldType;
import crain.model.domain.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.DomainObjectUtil;

/**
 * Unit test for the {@link Player} class
 */
@DisplayName("Player Testing")
public class PlayerTest {

    @Test
    public void softEqualityTest() {

        Player playerOne = DomainObjectUtil.createTestPlayer("Player One", WorldType.MULTIWORLD, 1);
        Player playerTwo = DomainObjectUtil.createTestPlayer("Player Two", WorldType.MULTIWORLD, 2);
        Assertions.assertFalse(playerOne.softEqualityWithPlayer(playerTwo));

        playerTwo.setPlayerName("Player One");
        Assertions.assertTrue(playerOne.softEqualityWithPlayer(playerTwo));

        playerTwo.setPlayerName("Player Two");
        playerTwo.setWorldId(1);
        playerTwo.setWorldType(WorldType.SHARED);
        playerOne.setWorldType(WorldType.SHARED);
        Assertions.assertFalse(playerOne.softEqualityWithPlayer(playerTwo));

        playerTwo.setPlayerName("Player One");
        Assertions.assertTrue(playerOne.softEqualityWithPlayer(playerTwo));
    }
}
