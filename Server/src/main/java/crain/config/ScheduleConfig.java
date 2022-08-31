package crain.config;

import crain.model.event.GeneralMessageEvent;
import crain.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import records.ROOM;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableScheduling
public class ScheduleConfig {

    private final GameRoomService gameRoomService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Scheduled(cron="0 0 0 4 * *")
    public void clearEmptyGameRooms() {
        List<ROOM.GameRoomRecord> toDelete = gameRoomService.getEmptyGameRooms();
        toDelete.forEach(room -> applicationEventPublisher.publishEvent(new GeneralMessageEvent("This Room is being cleared due to Inactivity.",  room.name())));
        gameRoomService.clearEmptyGameRooms();
    }
}
