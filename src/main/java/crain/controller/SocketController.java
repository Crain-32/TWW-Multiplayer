package crain.controller;

import crain.model.dto.CoopDto;
import crain.model.dto.EventDto;
import crain.model.dto.MultiworldDto;
import crain.model.dto.PlayerDto;
import crain.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GameRoomService gameRoomService;

    @MessageMapping("/multiworld/{GameRoom}")
    public void postItemToGroupTopic(@Payload MultiworldDto dto, @DestinationVariable("GameRoom") String gameroom) {
        this.simpMessagingTemplate.convertAndSend("/topic/multiworld/" + gameroom, dto);
    }

    @MessageMapping("/coop/{GameRoom}")
    public void postItemToCoopTopic(@Payload CoopDto dto, @DestinationVariable("GameRoom") String gameroom) {
        this.simpMessagingTemplate.convertAndSend("/topic/coop/" + gameroom, dto);
    }



    @MessageMapping("/event/{GameRoom}")
    public void postEventToGroupTopic(@Payload EventDto eventDto, @DestinationVariable("GameRoom") String gameroom) {
        this.simpMessagingTemplate.convertAndSend("/topic/event/" + gameroom, eventDto);
    }

    @MessageMapping("/names/{GameRoom}")
    public void getNamesForGameRoom(@Payload PlayerDto playerDto, @DestinationVariable("GameRoom") String gameroom) {
        this.simpMessagingTemplate.convertAndSend("/topic/names/" + gameroom, "foo");
    }
}
