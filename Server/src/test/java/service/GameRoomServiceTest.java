package service;

import constants.WorldType;
import crain.config.DataConfig;
import crain.exceptions.InvalidGameRoomException;
import crain.exceptions.InvalidPlayerException;
import crain.mappers.GameRoomMapper;
import crain.model.domain.GameRoom;
import crain.repository.GameRoomRepo;
import crain.service.GameRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import records.INFO;
import records.ROOM;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringJUnitConfig(classes = GameRoomServiceTest.Config.class)
@DataJpaTest
@ComponentScan(basePackageClasses = {GameRoomService.class, GameRoom.class, GameRoomMapper.class}, lazyInit = true)
@DisplayName("Game Room Service Test")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class GameRoomServiceTest {

    private static final String baseGameRoomName = "Test";
    private static final String baseGameRoomPassword = "Test";
    @Autowired private GameRoomService gameRoomService;
    @Autowired private GameRoomRepo gameRoomRepo;

    @Configuration
    @Import(DataConfig.class)
    @EntityScan(basePackages = {"crain.model.domain"})
    @EnableJpaRepositories(basePackages = {"crain.repository"})
    static class Config {}

    @BeforeEach
    public void removeAllGameRooms() {
        gameRoomRepo.deleteAll();
    }
    @Test
    public void itShouldRequireAGameRoomToExist() {
        assertThrows(InvalidGameRoomException.class, () -> gameRoomService.addPlayerDto(new ROOM.PlayerRecord("Player 1", 1, WorldType.COOP, null), baseGameRoomName));
    }

    @Test
    public void itShouldGetGameRoomsByTimestamp() {
        gameRoomService.createGameRoom(getCreateRoomDto());
        GameRoom testingGameRoom = gameRoomRepo.findOneByName(baseGameRoomName).orElseThrow(TestAbortedException::new);

        Instant adjustedTime = testingGameRoom.getCreationTimestamp().plus(1, ChronoUnit.SECONDS);
        List<GameRoom> gameRoomsBefore = gameRoomRepo.findAllGameRoomsCreatedBefore(adjustedTime);
        assertEquals(1, gameRoomsBefore.size());

        adjustedTime = testingGameRoom.getCreationTimestamp().minus(1, ChronoUnit.SECONDS);
        gameRoomsBefore = gameRoomRepo.findAllGameRoomsCreatedBefore(adjustedTime);
        assertEquals(0, gameRoomsBefore.size());
    }

    @Test
    public void itShouldNotAllowDuplicateNamesInCoop() {
        gameRoomService.createGameRoom(getCreateRoomDto(WorldType.COOP));
        gameRoomService.addPlayerDto(new ROOM.PlayerRecord("Player 1", 1, WorldType.COOP, null), baseGameRoomName);
        assertDoesNotThrow(() -> gameRoomService.addPlayerDto(new ROOM.PlayerRecord("Player 2", 1, WorldType.COOP, null), baseGameRoomName));
        assertThrows(InvalidPlayerException.class, () -> gameRoomService.addPlayerDto(new ROOM.PlayerRecord("Player 2", 1, WorldType.COOP, null), baseGameRoomName));
    }

    @Test
    public void itShouldNotAllowDuplicateWorldsInMultiworld() {
        gameRoomService.createGameRoom(getCreateRoomDto());
        gameRoomService.addPlayerDto(new ROOM.PlayerRecord("Player 1", 1, WorldType.MULTIWORLD, null), baseGameRoomName);
        assertDoesNotThrow(() -> gameRoomService.addPlayerDto(new ROOM.PlayerRecord("Player 2", 2, WorldType.MULTIWORLD, null), baseGameRoomName));
        assertThrows(InvalidPlayerException.class, () -> gameRoomService.addPlayerDto(new ROOM.PlayerRecord("Player 3", 2, WorldType.MULTIWORLD, null), baseGameRoomName));
    }

    @Test
    public void itShouldAllowDuplicateWorldsInShared() {
        gameRoomService.createGameRoom(getCreateRoomDto());
        gameRoomService.addPlayerDto(new ROOM.PlayerRecord("Player 1", 1, WorldType.SHARED, null), baseGameRoomName);
        assertDoesNotThrow(() -> gameRoomService.addPlayerDto(new ROOM.PlayerRecord("Player 2", 1, WorldType.SHARED, null), baseGameRoomName));
    }

    @Test
    public void itShouldRequireBothWorldsToBeShared() {
        gameRoomService.createGameRoom(getCreateRoomDto());
        gameRoomService.addPlayerDto(new ROOM.PlayerRecord("Player 1", 1, WorldType.SHARED, null), baseGameRoomName);
        assertThrows(InvalidPlayerException.class, () -> gameRoomService.addPlayerDto(new ROOM.PlayerRecord("Player 2", 1, WorldType.MULTIWORLD, null), baseGameRoomName));

        gameRoomService.deleteGameRoomByName(baseGameRoomName);
        gameRoomService.createGameRoom(getCreateRoomDto());
        gameRoomService.addPlayerDto(new ROOM.PlayerRecord("Player 1", 1, WorldType.MULTIWORLD, null), baseGameRoomName);
        assertThrows(InvalidPlayerException.class, () -> gameRoomService.addPlayerDto(new ROOM.PlayerRecord("Player 2", 1, WorldType.SHARED, null), baseGameRoomName));
    }

    @Test
    public void itShouldCountConnectedPlayers() {
        gameRoomService.createGameRoom(getCreateRoomDto());
        var playerOne =new ROOM.PlayerRecord("Player 1", 1, WorldType.SHARED, null);
        var playerTwo =new ROOM.PlayerRecord("Player 2", 1, WorldType.SHARED, null);
        gameRoomService.addPlayerDto(playerOne, baseGameRoomName);
        gameRoomService.addPlayerDto(playerTwo, baseGameRoomName);
        var gameRoom = gameRoomRepo.findOneByName(baseGameRoomName).orElseThrow(TestAbortedException::new);
        assertEquals(0, gameRoom.getConnectedPlayerCount());
        gameRoomService.setPlayerToConnected(playerOne, baseGameRoomName);
        gameRoom = gameRoomRepo.findOneByName(baseGameRoomName).orElseThrow(TestAbortedException::new);
        assertEquals(1, gameRoom.getConnectedPlayerCount());
        gameRoomService.setPlayerToConnected(playerTwo, baseGameRoomName);
        gameRoom = gameRoomRepo.findOneByName(baseGameRoomName).orElseThrow(TestAbortedException::new);
        assertEquals(2, gameRoom.getConnectedPlayerCount());
    }

    private INFO.CreateRoomRecord getCreateRoomDto() {
        return getCreateRoomDto(WorldType.MULTIWORLD);
    }
    private INFO.CreateRoomRecord getCreateRoomDto(WorldType worldType) {
        return new INFO.CreateRoomRecord(baseGameRoomName, 2, 2, baseGameRoomPassword, worldType);
    }
}
