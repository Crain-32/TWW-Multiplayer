package crain.controller;

import crain.exceptions.RoomException;
import crain.model.dto.EventDto;
import crain.model.dto.ItemDto;
import crain.model.dto.PlayerDto;
import crain.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class GameFlowController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GameRoomService gameRoomService;

    @MessageMapping("/item/{GameRoom}")
    public void postItemToGroupTopic(@Payload ItemDto dto, @DestinationVariable("GameRoom") String gameRoomName) {
        gameRoomService.givePlayerInGameRoomItem(gameRoomName, dto);
        simpMessagingTemplate.convertAndSend("/topic/multiplayer/" + gameRoomName, dto);
    }

    @MessageMapping("/event/{GameRoom}")
    public void postEventToGroupTopic(@Payload EventDto eventDto, @DestinationVariable("GameRoom") String gameroom) {
        simpMessagingTemplate.convertAndSend("/topic/event/" + gameroom, eventDto);
    }

    @MessageMapping("/connect/{GameRoom}")
    public void setPlayerToConnected(@Valid @Payload PlayerDto playerDto,
                                     @DestinationVariable("GameRoom") String gameroom,
                                     @Header("password") String password) {
        gameRoomService.setPlayerToConnected(playerDto, gameroom, password);
        simpMessagingTemplate.convertAndSend("/topic/names/" + gameroom, playerDto.getPlayerName());
    }

    @MessageExceptionHandler(value = {RoomException.class})
    public void handleInvalidObjExceptions(RoomException e) {
        if (Objects.nonNull(e.getGameRoomName())) {
            simpMessagingTemplate.convertAndSend("/topic/error/" + e.getGameRoomName(), e.getMessage());
        }
    }
}
