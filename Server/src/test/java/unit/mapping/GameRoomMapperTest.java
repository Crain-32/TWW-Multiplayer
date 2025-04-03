package unit.mapping;

import constants.WorldType;
import crain.mappers.GameRoomMapper;
import crain.mappers.PlayerMapper;
import crain.model.domain.GameRoom;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.util.ReflectionTestUtils;
import records.ROOM;
import crain.util.DomainObjectUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Game Room Mapper Test")
@Import(GameRoomMapper.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
class GameRoomMapperTest {

    static GameRoomMapper gameRoomMapper;

    @BeforeAll
    public static void setup() {
        gameRoomMapper = Mappers.getMapper(GameRoomMapper.class);
        var playerMapper = Mappers.getMapper(PlayerMapper.class);
        ReflectionTestUtils.setField(gameRoomMapper, "playerMapper", playerMapper);
    }

    @Test
    public void itShouldMapAGameRoomEntityCorrect() {
        GameRoom testInstance = new GameRoom();
        testInstance.setName("Test Room");
        testInstance.setPassword("Test Password");
        testInstance.setWorldAmount(2);
        testInstance.addPlayer(DomainObjectUtil.createTestPlayer("Player 1", WorldType.SHARED, 1));
        testInstance.addPlayer(DomainObjectUtil.createTestPlayer("Player 2", WorldType.SHARED, 2));
        ROOM.GameRoomRecord gameRoomRecord = gameRoomMapper.gameRoomRecordMapper(testInstance);

        assertEquals(testInstance.getName(), gameRoomRecord.name());
        assertTrue(gameRoomRecord.extraValidation());
        assertEquals(testInstance.getWorldAmount(), gameRoomRecord.worldAmount());
        assertEquals(testInstance.getPlayers().getFirst().getPlayerName(), gameRoomRecord.players().getFirst().playerName());
        assertEquals(testInstance.getPlayers().getFirst().getWorldType(), gameRoomRecord.players().getFirst().worldType());
        assertEquals(testInstance.getPlayers().getFirst().getWorldId(), gameRoomRecord.players().getFirst().worldId());

        assertEquals(testInstance.getPlayers().get(1).getPlayerName(), gameRoomRecord.players().get(1).playerName());
        assertEquals(testInstance.getPlayers().get(1).getWorldType(), gameRoomRecord.players().get(1).worldType());
        assertEquals(testInstance.getPlayers().get(1).getWorldId(), gameRoomRecord.players().get(1).worldId());
    }
}
