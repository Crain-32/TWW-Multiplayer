package client.communication;

import client.communication.handlers.AbstractQueueHandler;
import client.config.GameRoomConfig;
import client.events.ItemFoundEvent;
import constants.WorldType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import records.INFO;

import java.util.List;

@Slf4j
@Component("stompSessionHandler")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS) // Required for StompService to Construct.
public class StompSessionHandler extends StompSessionHandlerAdapter {

    private final List<? extends AbstractQueueHandler> queueHandlers;
    private final GameRoomConfig gameRoomConfig;
    private StompSession stompSession;

    public StompSessionHandler(@Qualifier("queueHandler") List<? extends AbstractQueueHandler> queueHandlers, GameRoomConfig gameRoomConfig) {
        this.queueHandlers = queueHandlers;
        this.gameRoomConfig = gameRoomConfig;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.setAutoReceipt(true);
        log.debug(gameRoomConfig.getGameRoomName());
        try {
            for (AbstractQueueHandler handler : queueHandlers) {
                WorldType handlerWorldType = handler.supportedWorldType();
                if (handlerWorldType != WorldType.SHARED && handlerWorldType != gameRoomConfig.getWorldType()) {
                    log.debug("Now Skipping Registration: " + handler.getClass().getSimpleName());
                    continue;
                }
                log.debug("Now Registering: " + handler.getClass().getSimpleName());
                StompHeaders subscriptionHeaders = createNormalHeaders(handler.getTopicPath() + gameRoomConfig.getGameRoomName());
                log.debug("Now Subscribing to: " + subscriptionHeaders.getDestination());
                session.subscribe(subscriptionHeaders, handler);
            }
        } catch (Exception e) {
            log.info("An Unknown Exception Occurred", e);
        }
        this.stompSession = session;
        // Need to send /app/connect/gameroom PlayerRecord after at this line.
    }

    @Async
    @EventListener
    public void sendMultiworldItem(ItemFoundEvent event) {
        Integer itemId = Byte.toUnsignedInt(event.getInfo().getItemId());
        if (gameRoomConfig.getWorldType() == WorldType.COOP) {
            log.debug("Sending Coop Item");

            var coopRecord = new INFO.CoopItemRecord(gameRoomConfig.getPlayerName(), itemId);
            StompHeaders headers = createNormalHeaders("/app/coop/" + gameRoomConfig.getGameRoomName());
            stompSession.send(headers, coopRecord);
        } else {
            log.debug("Sending Multiworld Item");

            var multiworldRecord =new INFO.ItemRecord(gameRoomConfig.getWorldId(), event.getWorldId(), itemId);
            StompHeaders headers = createNormalHeaders("/app/item/" + gameRoomConfig.getGameRoomName());
            stompSession.send(headers, multiworldRecord);
        }

    }


    @Async
    @EventListener
    public void sendEventInfo(INFO.EventRecord eventRecord) {
        log.debug("Sending Event Record");
        StompHeaders headers = createNormalHeaders("/app/event/" + gameRoomConfig.getGameRoomName());
        stompSession.send(headers, eventRecord);
    }

    private StompHeaders createNormalHeaders(String destination) {
        StompHeaders headers = new StompHeaders();
        headers.setDestination(destination);
        headers.set("password", gameRoomConfig.getPassword());
        return headers;
    }
}
