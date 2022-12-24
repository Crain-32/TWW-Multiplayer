package unit.mapping;

import constants.WorldType;
import crain.mappers.GameRoomMapper;
import crain.model.domain.GameRoom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import records.ROOM;
import util.DomainObjectUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Game Room Mapper Test")
@Import(GameRoomMapper.class)
class GameRoomMapperTest {

    @Autowired
    GameRoomMapper gameRoomMapper;

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
        assertEquals(testInstance.getPlayers().get(0).getPlayerName(), gameRoomRecord.players().get(0).playerName());
        assertEquals(testInstance.getPlayers().get(0).getWorldType(), gameRoomRecord.players().get(0).worldType());
        assertEquals(testInstance.getPlayers().get(0).getWorldId(), gameRoomRecord.players().get(0).worldId());

        assertEquals(testInstance.getPlayers().get(1).getPlayerName(), gameRoomRecord.players().get(1).playerName());
        assertEquals(testInstance.getPlayers().get(1).getWorldType(), gameRoomRecord.players().get(1).worldType());
        assertEquals(testInstance.getPlayers().get(1).getWorldId(), gameRoomRecord.players().get(1).worldId());
    }
}
