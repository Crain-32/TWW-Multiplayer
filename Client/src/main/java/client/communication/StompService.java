package client.communication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class StompService {

    @Autowired // Required due to Proxying
    private StompSessionHandler stompSessionHandler;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @Value("ws://${multiplayer.server.url}:${multiplayer.server.port}/ws")
    private String targetURL;


    public void setUpClient() {
        log.trace("Creating Websocket connection to the following URL: {}", targetURL);
        WebSocketClient baseClient = new StandardWebSocketClient();
        WebSocketStompClient client = new WebSocketStompClient(baseClient);
        client.setMessageConverter(new MappingJackson2MessageConverter());
        client.setTaskScheduler(threadPoolTaskScheduler);
        client.connect(targetURL, stompSessionHandler);
    }
}
