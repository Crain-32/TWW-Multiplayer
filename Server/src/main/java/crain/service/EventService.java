package crain.service;

import crain.model.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import records.ROOM;

/**
 * Swapping to an Event-Base Handling for the Messaging.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EventService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Async
    @EventListener
    public void MultiworldItemEventListener(MultiworldItemEvent itemEvent) {
        if (log.isDebugEnabled()) {
            log.debug("Multiworld Item Record: " + itemEvent.itemRecord() + " GameRoom: " + itemEvent.gameRoom());
        }
        simpMessagingTemplate.convertAndSend("/topic/multiplayer/" + itemEvent.gameRoom(), itemEvent.itemRecord());
    }

    @Async
    @EventListener
    public void CoopItemEventListener(CoopItemEvent itemEvent) {
        if (log.isDebugEnabled()) {
            log.debug("Coop Item Record: " + itemEvent.itemRecord() + " GameRoom: " + itemEvent.gameRoom());
        }
        simpMessagingTemplate.convertAndSend("/topic/coop/" + itemEvent.gameRoom(), itemEvent.itemRecord());
    }

    @Async
    @EventListener
    public void EventEventListener(EventEvent eventEvent) {
        if (log.isDebugEnabled()) {
            log.debug("Event Record: " + eventEvent.eventRecord() + " GameRoom: " + eventEvent.gameRoom());
        }
        simpMessagingTemplate.convertAndSend("/topic/event/" + eventEvent.gameRoom(), eventEvent.eventRecord());
    }

    @Async
    @EventListener
    public void NameEventListener(NameEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Player Record: " + event.playerRecord() + " GameRoom: " + event.gameRoom());
        }
        simpMessagingTemplate.convertAndSend("/topic/names/" + event.gameRoom(), event.playerRecord());
    }

    @Async
    @EventListener
    public void GameRoomMessageEventListener(GameRoomMessageEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("General Message: " + event.message() + " GameRoom: " + event.gameRoom());
        }
        simpMessagingTemplate.convertAndSend("/topic/general/" + event.gameRoom(), new ROOM.MessageRecord(event.message()));
    }

    @Async
    @EventListener
    public void ErrorMessageEventListener(ErrorEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Error Event: " + event.message() + " GameRoom: " + event.gameRoom());
        }
        simpMessagingTemplate.convertAndSend("/topic/error/" + event.gameRoom(), new ROOM.ErrorRecord(event.message()));
    }

//    Not sure what we'll put in here yet tbh.
//    @Async
//    @EventListener
//    public void TournamentEventListener() {
//
//    }

}
