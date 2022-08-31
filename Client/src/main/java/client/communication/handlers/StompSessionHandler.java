package client.communication.handlers;

import client.communication.GameRoomConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import records.INFO;

@Component
@RequiredArgsConstructor
public class StompSessionHandler extends StompSessionHandlerAdapter {

    private final NamesQueueHandler namesQueueHandler;
    private final MultiplayerQueueHandler multiplayerQueueHandler;
    private final GeneralQueueHandler generalQueueHandler;
    private final EventQueueHandler eventQueueHandler;
    private final ErrorQueueHandler errorQueueHandler;
    private final CoopQueueHandler coopQueueHandler;
    @Autowired
    private GameRoomConfig gameRoomConfig;
    private StompSession stompSession;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/topic/multiplayer/" + gameRoomConfig.getGameRoomName(), multiplayerQueueHandler);
        session.subscribe("/topic/coop/" + gameRoomConfig.getGameRoomName(), coopQueueHandler);
        session.subscribe("/topic/event/" + gameRoomConfig.getGameRoomName(), eventQueueHandler);
        session.subscribe("/topic/names/" + gameRoomConfig.getGameRoomName(), namesQueueHandler);
        session.subscribe("/topic/general/" + gameRoomConfig.getGameRoomName(), generalQueueHandler);
        session.subscribe("/topic/error/" + gameRoomConfig.getGameRoomName(), errorQueueHandler);
        this.stompSession = session;
        // Need to send /app/connect/gameroom PlayerRecord after at this line.
    }


    @EventListener
    public void sendMultiworldItem(INFO.ItemRecord itemRecord) {
        StompHeaders headers = createNormalHeaders("/app/item/" + gameRoomConfig);
        stompSession.send(headers, itemRecord);
    }

    @EventListener
    public void sendCoopItem(INFO.CoopItemRecord coopRecord) {
        StompHeaders headers = createNormalHeaders("/app/coop/" + gameRoomConfig);
        stompSession.send(headers, coopRecord);
    }

    @EventListener
    public void sendEventInfo(INFO.EventRecord eventRecord) {
        StompHeaders headers = createNormalHeaders("/app/event/" + gameRoomConfig);
        stompSession.send(headers, eventRecord);
    }

    private StompHeaders createNormalHeaders(String destination) {
        StompHeaders headers = new StompHeaders();
        headers.setDestination(destination);
        headers.set("password", gameRoomConfig.getPassword());
        return headers;
    }
}
