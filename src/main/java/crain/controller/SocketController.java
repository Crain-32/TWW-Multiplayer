package crain.controller;

import crain.model.dto.EventDto;
import crain.model.dto.ItemDto;
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

    @MessageMapping("/item/{GameRoom}")
    public void postItemToGroupTopic(@Payload ItemDto input, @DestinationVariable("GameRoom") String gameroom) {
        this.simpMessagingTemplate.convertAndSend("/topic/item/" + gameroom, input);
    }

    @MessageMapping("/event/{GameRoom}")
    public void postEventToGroupTopic(@Payload EventDto eventDto, @DestinationVariable("GameRoom") String gameroom) {
        this.simpMessagingTemplate.convertAndSend("/topic/event/" + gameroom, eventDto);
    }

    @MessageMapping("/names/{GameRoom}")
    public void getNamesForGameRoom(@DestinationVariable("GameRoom") String gameroom) {

    }
}
