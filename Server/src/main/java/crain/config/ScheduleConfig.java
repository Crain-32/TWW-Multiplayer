package crain.config;

import crain.model.event.GameRoomMessageEvent;
import crain.service.GameRoomService;
import crain.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import records.ROOM;

import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ScheduleConfig {

    private final PlayerService playerService;
    private final GameRoomService gameRoomService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Scheduled(cron = "0 0 * ? * * ") // Every Hour on the Hour
    public void setPlayersToDisconnected() {
        log.info("Running: 'setPlayersToDisconnected()'");
        playerService.setOldPlayersToDisconnected();
    }

    @Scheduled(cron = "0 5 * ? * * ") // Every Hour 5 Minutes after the Hour
    public void clearEmptyGameRooms() {
        log.info("Running: 'clearEmptyGameRooms()'");
        gameRoomService.getEmptyGameRooms()
                .stream()
                .map(room -> new GameRoomMessageEvent("This Room is being cleared due to Inactivity.", room.name()))
                .forEach(applicationEventPublisher::publishEvent);
        gameRoomService.clearEmptyGameRooms();
    }
}
