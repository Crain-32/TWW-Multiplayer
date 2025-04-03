package crain.service;

import crain.model.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Swapping to an Event-Base Handling for the Messaging.
 */
@Slf4j
@Component
public class EventService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public EventService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Async
    @EventListener
    public void multiplayerEventListener(MultiplayerEvent<?> event) {
        log.atDebug()
                .addKeyValue("Event Type", event.getClass().toString())
                .addKeyValue("GameRoom", event.getGameRoomName())
                .log();
        String path = "/topic/" + switch (event) {
            case MultiworldItemEvent ignored -> "multiplayer/";
            case CoopItemEvent ignored -> "coop/";
            case EventEvent ignored -> "event/";
            case NameEvent ignored -> "names/";
            case GameRoomMessageEvent ignored -> "general/";
            case ErrorEvent ignored -> "error/";
        };
        simpMessagingTemplate.convertAndSend(path + event.getGameRoomName(), event.getPayload());
    }
}
