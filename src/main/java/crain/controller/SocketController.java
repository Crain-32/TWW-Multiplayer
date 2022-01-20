package crain.controller;

import crain.model.dto.ConnectionRequestDto;
import crain.model.dto.ItemDto;
import crain.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class SocketController {


    private final GameRoomService gameRoomService;
    private static final String GAME_ROOM_QUEUE = "/queue/gameroom/";

    @SneakyThrows
    @MessageMapping("/item/{gameRoomName}")
    public void handleItems(@PathVariable("gameRoomName") String gameRoomName, @Payload ItemDto item, StompSession stompSession) {
        String gameRoomUrl = gameRoomService.receiveItem(gameRoomName, item);
        stompSession.send(GAME_ROOM_QUEUE + gameRoomUrl, item);
    }

    @MessageMapping("/ready")
    @SendToUser("/ready/queue")
    public String handleConnect(@Payload ConnectionRequestDto dto, Principal user) {
        return gameRoomService.connectPlayer(dto);
    }

    @MessageMapping("/test")
    public ItemDto testOther(@Payload ItemDto input) {
        return input;
    }
}
