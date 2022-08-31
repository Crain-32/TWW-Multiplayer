package client.communication;

import client.communication.handlers.StompSessionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Service
@RequiredArgsConstructor
public class StompService {

    private WebSocketStompClient client;
    private final StompSessionHandler stompSessionHandler;
    @Value("${multiplayer.server.url}:${multiplayer.server.port}")
    private String targetURL;


    public void setUpClient() {
        WebSocketClient baseClient = new StandardWebSocketClient();
        client = new WebSocketStompClient(baseClient);
        client.setMessageConverter(new MappingJackson2MessageConverter());
        client.connect(targetURL, stompSessionHandler);
    }
}
