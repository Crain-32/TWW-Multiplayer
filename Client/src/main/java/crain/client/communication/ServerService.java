package crain.client.communication;

import constants.WorldType;
import crain.client.config.GameRoomConfig;
import crain.client.events.SetConfigEvent;
import crain.client.game.GameInfoConfig;
import crain.client.view.events.GeneralMessageEvent;
import crain.client.view.events.ServerConnectEvent;
import crain.client.view.events.ServerDisconnectEvent;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import records.INFO;
import records.ROOM;

import java.util.Objects;
import java.util.Optional;

@Component
public class ServerService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ServerService.class);
    private final GameRoomApi gameRoomApi;
    private final StompService stompService;
    private final GameRoomConfig gameRoomConfig;
    private final GameInfoConfig gameInfoConfig;
    private final ApplicationEventPublisher applicationEventPublisher;
    private boolean connectedToRoom = false;

    public ServerService(@Value("${https://${multiplayer.server.url}:${multiplayer.server.port}/rest/gameroom}") String serverBaseUrl, StompService stompService, GameRoomConfig gameRoomConfig, GameInfoConfig gameInfoConfig, ApplicationEventPublisher applicationEventPublisher) {
        WebClient client = WebClient.builder().baseUrl(serverBaseUrl).build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();

        this.gameRoomApi = factory.createClient(GameRoomApi.class);
        this.stompService = stompService;
        this.gameRoomConfig = gameRoomConfig;
        this.gameInfoConfig = gameInfoConfig;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Async
    @EventListener
    // Technically this needs to check for the Dev Server URL changing as well, since now that is actually dynamic.
    public void handleConfigChange(SetConfigEvent configEvent) {
        gameRoomConfig.setGameRoomName(Optional.ofNullable(configEvent.getGameRoomName()).orElse(gameRoomConfig.getGameRoomName()));
        gameRoomConfig.setPassword(Optional.ofNullable(configEvent.getPassword()).orElse(gameRoomConfig.getPassword()));
        gameRoomConfig.setPlayerName(Optional.ofNullable(configEvent.getPlayerName()).orElse(gameRoomConfig.getPlayerName()));
        gameRoomConfig.setWorldId(Optional.ofNullable(configEvent.getWorldId()).orElse(gameRoomConfig.getWorldId()));
        gameRoomConfig.setPlayerAmount(Optional.ofNullable(configEvent.getPlayerAmount()).orElse(gameRoomConfig.getPlayerAmount()));
        gameRoomConfig.setWorldAmount(Optional.ofNullable(configEvent.getWorldAmount()).orElse(gameRoomConfig.getWorldAmount()));
        gameRoomConfig.setWorldType(Optional.ofNullable(configEvent.getWorldType()).orElse(gameRoomConfig.getWorldType()));
        gameInfoConfig.setWorldType(Optional.ofNullable(configEvent.getWorldType()).orElse(gameRoomConfig.getWorldType()));
        gameRoomConfig.setRoomType(Optional.ofNullable(configEvent.getRoomType()).orElse(gameRoomConfig.getRoomType()));
    }

    @Async
    @EventListener(CreateRoomEvent.class)
    public void createGameRoom() {
        try {
            INFO.CreateRoomRecord roomRecord = createRoomRecordFromConfig();
            gameRoomApi.createGameRoom(roomRecord);
            applicationEventPublisher.publishEvent(new GeneralMessageEvent("Successfully Created " + roomRecord.gameRoomName()));
        } catch (Exception e) {
            log.error("Game Room Client Failure", e);
            String response = e.getMessage();
            String serverResponse = response != null ? response : "Failed to Parse Server Response";
            applicationEventPublisher.publishEvent(new GeneralMessageEvent(serverResponse));
        }
    }

    @Async
    @EventListener(CreatePlayerEvent.class)
    public void createPlayer() {
        try {
            // check the player status first please
            Boolean createdPlayer = gameRoomApi.addPlayerToGameRoom(gameRoomConfig.getGameRoomName(), gameRoomConfig.getPassword(), createPlayerRecordFromConfig());
            String outputMessage;
            if (!createdPlayer) {
                outputMessage = "Failed to add your Player to the Game Room!";
            } else {
                outputMessage = gameRoomConfig.getPlayerName() + " was added to " + gameRoomConfig.getGameRoomName();
            }
            applicationEventPublisher.publishEvent(new GeneralMessageEvent(outputMessage));
        } catch (Exception e) {
            log.error("Failed to create Player", e);
            applicationEventPublisher.publishEvent(new GeneralMessageEvent(e.getMessage()));
        }
    }


    @Async
    @EventListener(ConnectToGameRoom.class)
    public void connectToRoom() {
        if (connectedToRoom) {
            return;
        }
        log.trace("Attempting to create Player and connect to the Room");
        createPlayer();
        stompService.setUpClient();
        connectedToRoom = true;
        applicationEventPublisher.publishEvent(new ServerConnectEvent());
    }

    @Async
    @EventListener(DisconnectFromGameRoom.class)
    public void disconnectFromServer() {
        if (!connectedToRoom) {
            return;
        }
        log.trace("Attempting to close the connection to the server");
        try {
            stompService.disconnectFromServer();
            connectedToRoom = false;
            applicationEventPublisher.publishEvent(new ServerDisconnectEvent());
        } catch (Exception e) {
            log.debug("Failed to Disconnect from the Server", e);
        }
    }


    private INFO.CreateRoomRecord createRoomRecordFromConfig() {
        return new INFO.CreateRoomRecord(
                gameRoomConfig.getGameRoomName(),
                Objects.requireNonNullElse(gameRoomConfig.getPlayerAmount(), 1),
                Objects.requireNonNullElse(gameRoomConfig.getWorldAmount(), 1),
                gameRoomConfig.getPassword(),
                Objects.requireNonNullElse(gameRoomConfig.getWorldType(), WorldType.SHARED)
        );
    }

    private ROOM.PlayerRecord createPlayerRecordFromConfig() {
        return new ROOM.PlayerRecord(
                gameRoomConfig.getPlayerName(),
                gameRoomConfig.getWorldId(),
                Objects.requireNonNullElse(gameRoomConfig.getWorldType(), WorldType.SHARED),
                false
        );
    }

    public static class CreateRoomEvent {
    }

    public static class CreatePlayerEvent {
    }

    public static class ConnectToGameRoom {

    }

    public static class DisconnectFromGameRoom {

    }
}
