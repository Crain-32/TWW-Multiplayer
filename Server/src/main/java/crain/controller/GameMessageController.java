package crain.controller;

import crain.exceptions.RoomException;
import crain.model.event.CoopItemEvent;
import crain.model.event.ErrorEvent;
import crain.model.event.EventEvent;
import crain.model.event.NameEvent;
import crain.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import records.INFO;
import records.ROOM;

import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class GameMessageController {
    private final GameRoomService gameRoomService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @MessageMapping("/item/{GameRoom}")
    public void postItemToGroupTopic(@Payload INFO.ItemRecord dto, @DestinationVariable("GameRoom") String gameRoomName) {
        gameRoomService.givePlayerInGameRoomItem(gameRoomName, dto);
    }

    @MessageMapping("/event/{GameRoom}")
    public void postEventToGroupTopic(@Payload INFO.EventRecord eventRecord, @DestinationVariable("GameRoom") String gameroom) {
        applicationEventPublisher.publishEvent(new EventEvent(eventRecord, gameroom));
    }

    @MessageMapping("/connect/{GameRoom}")
    public void setPlayerToConnected(@Valid @Payload ROOM.PlayerRecord playerRecord,
                                     @DestinationVariable("GameRoom") String gameroom) {
        playerRecord = gameRoomService.setPlayerToConnected(playerRecord, gameroom);
        applicationEventPublisher.publishEvent(new NameEvent(playerRecord, gameroom));
    }


    @MessageMapping("/coop/{GameRoom}")
    public void postItemToCoopTopic(@Valid @Payload INFO.CoopItemRecord coopItemRecord,
                                    @DestinationVariable("GameRoom") String gameroom) {
        applicationEventPublisher.publishEvent(new CoopItemEvent(coopItemRecord, gameroom));
    }


    @MessageExceptionHandler(value = {RoomException.class})
    public void handleInvalidObjExceptions(RoomException e) {
        if (Objects.nonNull(e.getGameRoomName())) {
            applicationEventPublisher.publishEvent(new ErrorEvent(e.getMessage(), e.getGameRoomName()));
        }
    }
}
