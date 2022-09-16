package service;

import constants.WorldType;
import crain.mappers.GameRoomMapper;
import crain.model.domain.GameRoom;
import crain.repository.GameRoomRepo;
import crain.service.GameRoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import records.INFO;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    @EntityScan(basePackages = {"crain.model.domain"})
    @EnableJpaRepositories(basePackages = {"crain.repository"})
    static class Config {}

    @Test
    public void itShouldGetGameRoomsByTimestamp() {
        gameRoomService.createGameRoom(getCreateRoomDto());
        GameRoom testingGameRoom = gameRoomRepo.findOneByName(baseGameRoomName).orElseThrow(TestAbortedException::new);

        Instant adjustedTime = testingGameRoom.getCreationTimestamp().toInstant().plus(1, ChronoUnit.SECONDS);
        List<GameRoom> gameRoomsBefore = gameRoomRepo.findAllGameRoomsCreatedBefore(Timestamp.from(adjustedTime));
        assertEquals(1, gameRoomsBefore.size());

        adjustedTime = testingGameRoom.getCreationTimestamp().toInstant().minus(1, ChronoUnit.SECONDS);
        gameRoomsBefore = gameRoomRepo.findAllGameRoomsCreatedBefore(Timestamp.from(adjustedTime));
        assertEquals(0, gameRoomsBefore.size());
    }


    private INFO.CreateRoomRecord getCreateRoomDto() {
        return new INFO.CreateRoomRecord(baseGameRoomName, 2, 2, baseGameRoomPassword, WorldType.MULTIWORLD);
    }
}
