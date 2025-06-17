package crain.controller;

import crain.exceptions.RoomException;
import crain.model.event.CoopItemEvent;
import crain.model.event.ErrorEvent;
import crain.model.event.EventEvent;
import crain.model.event.NameEvent;
import crain.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import records.INFO;
import records.ROOM;

import jakarta.validation.Valid;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GameMessageController {
    private final GameRoomService gameRoomService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @MessageMapping("/item/{GameRoom}")
    public void postItemToGroupTopic(@Payload INFO.ItemRecord dto, @DestinationVariable("GameRoom") String gameRoom) {
        gameRoomService.givePlayerInGameRoomItem(gameRoom, dto);
    }

    @MessageMapping("/event/{GameRoom}")
    public void postEventToGroupTopic(@Payload INFO.EventRecord eventRecord, @DestinationVariable("GameRoom") String gameRoom) {
        applicationEventPublisher.publishEvent(new EventEvent(gameRoom, eventRecord));
    }

    @MessageMapping("/name/{GameRoom}")
    public void setPlayerToConnected(@Valid @Payload ROOM.PlayerRecord playerRecord,
                                     @DestinationVariable("GameRoom") String gameRoom) {
        playerRecord = gameRoomService.setPlayerToConnected(playerRecord, gameRoom);
        log.debug("{} was set to Connected in - {}", playerRecord.playerName(), gameRoom);
        applicationEventPublisher.publishEvent(new NameEvent(gameRoom, playerRecord));
    }


    @MessageMapping("/coop/{GameRoom}")
    public void postItemToCoopTopic(@Valid @Payload INFO.CoopItemRecord coopItemRecord,
                                    @DestinationVariable("GameRoom") String gameRoom) {
        applicationEventPublisher.publishEvent(new CoopItemEvent(gameRoom, coopItemRecord));
    }


    @MessageExceptionHandler(value = {RoomException.class})
    public void handleInvalidObjExceptions(RoomException e) {
        if (Objects.nonNull(e.getGameRoomName())) {
            applicationEventPublisher.publishEvent(new ErrorEvent(e.getGameRoomName(), e.getMessage()));
        }
    }
}
