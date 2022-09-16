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
            log.debug("Multiworld Item Record: " + itemEvent.getItemRecord() + " GameRoom: " + itemEvent.getGameRoom());
        }
        simpMessagingTemplate.convertAndSend("/topic/multiplayer/" + itemEvent.getGameRoom(), itemEvent.getItemRecord());
    }

    @Async
    @EventListener
    public void CoopItemEventListener(CoopItemEvent itemEvent) {
        if (log.isDebugEnabled()) {
            log.debug("Coop Item Record: " + itemEvent.getItemRecord() + " GameRoom: " + itemEvent.getGameRoom());
        }
        simpMessagingTemplate.convertAndSend("/topic/coop/" + itemEvent.getGameRoom(), itemEvent.getItemRecord());
    }

    @Async
    @EventListener
    public void EventEventListener(EventEvent eventEvent) {
        if (log.isDebugEnabled()) {
            log.debug("Event Record: " + eventEvent.getEventRecord() + " GameRoom: " + eventEvent.getGameRoom());
        }
        simpMessagingTemplate.convertAndSend("/topic/event/" + eventEvent.getGameRoom(), eventEvent.getEventRecord());
    }

    @Async
    @EventListener
    public void NameEventListener(NameEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Player Record: " + event.getPlayerRecord() + " GameRoom: " + event.getGameRoom());
        }
        simpMessagingTemplate.convertAndSend("/topic/names/" + event.getGameRoom(), event.getPlayerRecord());
    }

    @Async
    @EventListener
    public void GameRoomMessageEventListener(GameRoomMessageEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("General Message: " + event.getMessage() + " GameRoom: " + event.getGameRoom());
        }
        simpMessagingTemplate.convertAndSend("/topic/general/" + event.getGameRoom(), new ROOM.MessageRecord(event.getMessage()));
    }

    @Async
    @EventListener
    public void ErrorMessageEventListener(ErrorEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Error Event: " + event.getMessage() + " GameRoom: " + event.getGameRoom());
        }
        simpMessagingTemplate.convertAndSend("/topic/error/" + event.getGameRoom(), new ROOM.ErrorRecord(event.getMessage()));
    }

//    Not sure what we'll put in here yet tbh.
//    @Async
//    @EventListener
//    public void TournamentEventListener() {
//
//    }

}
