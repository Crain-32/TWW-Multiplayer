package crain.service;

import crain.model.event.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Swapping to an Event-Base Handling for the Messaging.
 */
@Component
@RequiredArgsConstructor
public class EventService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Async
    @EventListener
    public void MultiworldItemEventListener(MultiworldItemEvent itemEvent) {
        simpMessagingTemplate.convertAndSend("/topic/multiplayer/" + itemEvent.getGameRoom(), itemEvent.getItemRecord());
    }

    @Async
    @EventListener
    public void CoopItemEventListener(CoopItemEvent itemEvent) {
        simpMessagingTemplate.convertAndSend("/topic/coop/" + itemEvent.getGameRoom(), itemEvent.getItemRecord());
    }

    @Async
    @EventListener
    public void EventEventListener(EventEvent eventEvent) {
        simpMessagingTemplate.convertAndSend("/topic/event/" + eventEvent.getGameRoom(), eventEvent.getEventRecord());
    }

    @Async
    @EventListener
    public void NameEventListener(NameEvent event) {
        simpMessagingTemplate.convertAndSend("/topic/names/" + event.getGameRoom(),  event.getPlayerRecord());
    }

    @Async
    @EventListener
    public void GeneralMessageEventListener(GeneralMessageEvent event) {
        simpMessagingTemplate.convertAndSend("/topic/general/" + event.getGameRoom(), event.getMessage());
    }

    @Async
    @EventListener
    public void ErrorMessageEventListener(ErrorEvent event) {
        simpMessagingTemplate.convertAndSend("/topic/error/" + event.getGameRoom(), event.getMessage());
    }

//    Not sure what we'll put in here yet tbh.
//    @Async
//    @EventListener
//    public void TournamentEventListener() {
//
//    }

}
