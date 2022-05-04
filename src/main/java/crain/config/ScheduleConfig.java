package crain.config;

import crain.model.dto.GameRoomDto;
import crain.repository.GameRoomRepo;
import crain.repository.PlayerRepo;
import crain.service.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
public class ScheduleConfig {

    @Autowired
    private GameRoomService gameRoomService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Scheduled(initialDelay = 100000, fixedDelay = 10000000)
    public void clearEmptyGameRooms() {
        List<GameRoomDto> toDelete = gameRoomService.getEmptyGameRooms();
        toDelete.forEach(room -> {simpMessagingTemplate.convertAndSend("/topic/general/" + room.getName(), "This Room is being cleared due to Inactivity.");});
        gameRoomService.clearEmptyGameRooms();
    }
}
