package crain.controller;

import crain.model.dto.ConnectionRequestDto;
import crain.model.dto.ItemDto;
import crain.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class SocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GameRoomService gameRoomService;
    private static final String GAME_ROOM_QUEUE = "/queue/gameroom/";

    @SneakyThrows
    @MessageMapping("/item/{gameRoomName}")
    public void handleItems(@PathParam("gameRoomName") String gameRoomName, @Payload ItemDto item) {
        String gameRoomUrl = gameRoomService.receiveItem(gameRoomName, item);
        simpMessagingTemplate.convertAndSend(GAME_ROOM_QUEUE + gameRoomUrl, item);
    }

    @MessageMapping("/ready")
    @SendToUser("/ready/queue")
    public String handleConnect(@Payload ConnectionRequestDto dto, Principal user) {
        return gameRoomService.connectPlayer(dto);
    }

    @MessageMapping("/test")
    @SendToUser("/test/queue")
    public String testQueue(@Payload String test) {
        return test;
    }
}
