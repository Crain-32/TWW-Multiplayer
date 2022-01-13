package crain.controller;

import crain.model.dto.PlayerDto;
import crain.model.dto.SetUpDto;
import crain.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
@RequiredArgsConstructor
public class ServerController {

    private final GameRoomService gameRoomService;

    @MessageMapping("/setup")
    public void initialSetup(@Payload SetUpDto setUpDto) {
        gameRoomService.setUpGameRoom(setUpDto);
    }

    @MessageMapping("/new/player/{gameRoomName}")
    public void addPlayer(@PathParam("gameRoomName") String gameRoomName, @Payload PlayerDto playerRequest) {
        gameRoomService.addPlayer(playerRequest, gameRoomName);
    }
}
