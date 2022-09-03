package client.communication;

import client.events.SetConfigEvent;
import client.view.events.GeneralMessageEvent;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class ServerService {

    private final GameRoomApi gameRoomApi;
    private final StompService stompService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final GameRoomConfig gameRoomConfig;
    private boolean connectedToRoom = false;

    @Async
    @EventListener
    public void handleConfigChange(SetConfigEvent configEvent) {
        gameRoomConfig.setGameRoomName(Objects.requireNonNullElseGet(configEvent.getGameRoomName(), gameRoomConfig::getGameRoomName));
        gameRoomConfig.setPassword(Objects.requireNonNullElseGet(configEvent.getPassword(), gameRoomConfig::getPassword));
        gameRoomConfig.setPlayerName(Objects.requireNonNullElseGet(configEvent.getPlayerName(), gameRoomConfig::getPlayerName));
        gameRoomConfig.setWorldId(Objects.requireNonNullElseGet(configEvent.getWorldId(), gameRoomConfig::getWorldId));
        gameRoomConfig.setPlayerAmount(Objects.requireNonNullElseGet(configEvent.getPlayerAmount(), gameRoomConfig::getPlayerAmount));
        gameRoomConfig.setWorldAmount(Objects.requireNonNullElseGet(configEvent.getWorldAmount(), gameRoomConfig::getWorldAmount));
        gameRoomConfig.setWorldType(Objects.requireNonNullElseGet(configEvent.getWorldType(), gameRoomConfig::getWorldType));
        gameRoomConfig.setRoomType(Objects.requireNonNullElseGet(configEvent.getRoomType(), gameRoomConfig::getRoomType));
    }

    @Async
    @EventListener
    public void createGameRoom(CreateRoomEvent createRoomEvent) {
        try {
            gameRoomApi.createGameRoom(createRoomRecordFromConfig());
        } catch (FeignException.FeignClientException e) {
            ByteBuffer buf = e.responseBody().orElse(null);
            String serverResponse = buf != null ? buf.toString() : "Failed to Parse Server Response";
            applicationEventPublisher.publishEvent(new GeneralMessageEvent(serverResponse));
        } catch (Exception e) {
            log.debug(e.getLocalizedMessage());
            applicationEventPublisher.publishEvent(new GeneralMessageEvent("Failed to Communicate with the Server, please check your connection"));
        }
    }

    @Async
    @EventListener
    public void createPlayer(CreatePlayerEvent createPlayerEvent) {
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
            ByteBuffer buf = e.responseBody().orElse(null);
            String serverResponse = buf != null ? buf.toString() : "Failed to Parse Server Response";
            applicationEventPublisher.publishEvent(new GeneralMessageEvent(serverResponse));
        }
    }


    @Async
    @EventListener
    public void connectToRoom(ConnectToGameRoom connect) {
        if (connectedToRoom) {
            return;
        }
        stompService.setUpClient();
        connectedToRoom = true;
    }


    private INFO.CreateRoomRecord createRoomRecordFromConfig() {
        return new INFO.CreateRoomRecord(
                gameRoomConfig.getGameRoomName(),
                gameRoomConfig.getPlayerAmount(),
                gameRoomConfig.getWorldAmount(),
                gameRoomConfig.getPassword(),
                gameRoomConfig.getRoomType()
        );
    }

    private ROOM.PlayerRecord createPlayerRecordFromConfig() {
        return new ROOM.PlayerRecord(
                gameRoomConfig.getPlayerName(),
                gameRoomConfig.getWorldId(),
                gameRoomConfig.getWorldType(),
                false
        );
    }


    public static class CreateRoomEvent {
    }

    public static class CreatePlayerEvent {
    }

    public static class ConnectToGameRoom { }
}
