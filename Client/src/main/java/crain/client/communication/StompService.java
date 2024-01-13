package crain.client.communication;

import crain.client.service.SettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Slf4j
@Service
public class StompService {

    private final StompSessionHandler stompSessionHandler;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final SettingsService settingsService;
    // Migrate this to be a Settings Call
    @Value("ws://${multiplayer.server.url}:${multiplayer.server.port}/ws")
    private String targetURL;

    private WebSocketStompClient stompClient;

    public StompService(@Qualifier("stompSessionHandler") StompSessionHandler stompSessionHandler,
                        ThreadPoolTaskScheduler threadPoolTaskScheduler,
                        SettingsService settingsService) {
        this.stompSessionHandler = stompSessionHandler;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.settingsService = settingsService;
    }


    public void setUpClient() {
        log.trace("Creating Websocket connection to the following URL: {}", targetURL);
        WebSocketClient baseClient = new StandardWebSocketClient();
        WebSocketStompClient client = new WebSocketStompClient(baseClient);
        client.setMessageConverter(new MappingJackson2MessageConverter());
        client.setTaskScheduler(threadPoolTaskScheduler);
        client.connectAsync(targetURL, stompSessionHandler, stompSessionHandler);
        stompClient = client;
    }

    public void disconnectFromServer() {
        if (stompClient != null && stompClient.isRunning()) {
            stompClient.stop();
        }
    }
}
