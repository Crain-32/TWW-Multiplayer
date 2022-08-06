package crain.config;

import crain.service.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import records.ROOM;

import java.util.List;

@Configuration
@EnableScheduling
public class ScheduleConfig {

    @Autowired
    private GameRoomService gameRoomService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Scheduled(cron="0 0 0 4 * *")
    public void clearEmptyGameRooms() {
        List<ROOM.GameRoomRecord> toDelete = gameRoomService.getEmptyGameRooms();
        toDelete.forEach(room -> simpMessagingTemplate.convertAndSend("/topic/general/" + room.name(), "This Room is being cleared due to Inactivity."));
        gameRoomService.clearEmptyGameRooms();
    }
}