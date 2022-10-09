package client.communication;

import client.config.GameRoomConfig;
import client.events.SetConfigEvent;
import client.game.GameInfoConfig;
import client.view.events.GeneralMessageEvent;
import client.view.events.ServerConnectEvent;
import constants.WorldType;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import records.INFO;
import records.ROOM;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServerService {

    private final GameRoomApi gameRoomApi;
    private final StompService stompService;
    private final GameRoomConfig gameRoomConfig;
    private final GameInfoConfig gameInfoConfig;
    private final ApplicationEventPublisher applicationEventPublisher;
    private boolean connectedToRoom = false;

    @Async
    @EventListener
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
        } catch (FeignException.FeignClientException e) {
            log.error("Feign Client Failure", e);
            ByteBuffer buf = e.responseBody().orElse(null);
            String serverResponse = buf != null ? buf.toString() : "Failed to Parse Server Response";
            applicationEventPublisher.publishEvent(new GeneralMessageEvent(serverResponse));
        } catch (Exception e) {
            log.debug(e.getLocalizedMessage());
            applicationEventPublisher.publishEvent(new GeneralMessageEvent("Failed to Communicate with the Server, please verify your connection"));
        }
    }

    @Async
    @EventListener(CreatePlayerEvent.class)
    public void createPlayer() {
        try {
            Boolean createdPlayer = gameRoomApi.addPlayerToGameRoom(gameRoomConfig.getGameRoomName(), gameRoomConfig.getPassword(), createPlayerRecordFromConfig());
            String outputMessage;
            if (!createdPlayer) {
                outputMessage = "Failed to add your Player to the Game Room!";
            } else {
                outputMessage = gameRoomConfig.getPlayerName() + " was added to " + gameRoomConfig.getGameRoomName();
            }
            applicationEventPublisher.publishEvent(new GeneralMessageEvent(outputMessage));
        } catch (FeignException.FeignClientException e) {
            log.error("Failed to create Player", e);
            ByteBuffer buf = e.responseBody().orElse(null);
            String serverResponse = buf != null ? buf.toString() : "Failed to Parse Server Response";
            applicationEventPublisher.publishEvent(new GeneralMessageEvent(serverResponse));
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
