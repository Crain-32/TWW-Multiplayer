package crain.controller;

import crain.exceptions.RoomException;
import crain.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import records.INFO;
import records.ROOM;

import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class GameMessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GameRoomService gameRoomService;

    @MessageMapping("/item/{GameRoom}")
    public void postItemToGroupTopic(@Payload INFO.ItemRecord dto, @DestinationVariable("GameRoom") String gameRoomName) {
        gameRoomService.givePlayerInGameRoomItem(gameRoomName, dto);
        simpMessagingTemplate.convertAndSend("/topic/multiplayer/" + gameRoomName, dto);
    }

    @MessageMapping("/event/{GameRoom}")
    public void postEventToGroupTopic(@Payload INFO.EventRecord eventRecord, @DestinationVariable("GameRoom") String gameroom) {
        simpMessagingTemplate.convertAndSend("/topic/event/" + gameroom, eventRecord);
    }

    @MessageMapping("/connect/{GameRoom}")
    public void setPlayerToConnected(@Valid @Payload ROOM.PlayerRecord playerRecord,
                                     @DestinationVariable("GameRoom") String gameroom) {
        playerRecord = gameRoomService.setPlayerToConnected(playerRecord, gameroom);
        simpMessagingTemplate.convertAndSend("/topic/names/" + gameroom, playerRecord.playerName());
    }


    @MessageMapping("/coop/{GameRoom}")
    public void postItemToCoopTopic(@Valid @Payload INFO.CoopItemRecord coopItemRecord,
                                    @DestinationVariable("GameRoom") String gameroom) {
        simpMessagingTemplate.convertAndSend("/topic/coop/" + gameroom, coopItemRecord);
    }


    @MessageExceptionHandler(value = {RoomException.class})
    public void handleInvalidObjExceptions(RoomException e) {
        if (Objects.nonNull(e.getGameRoomName())) {
            simpMessagingTemplate.convertAndSend("/topic/error/" + e.getGameRoomName(), e.getMessage());
        }
    }
}
