package client.communication;

import client.events.SetConfigEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import records.INFO;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ServerService {

    private final GameRoomApi gameRoomApi;
    private final StompService stompService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final GameRoomConfig gameRoomConfig;

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
    public void createGameRoom(CreateRoomEvent e) {

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


    public static class CreateRoomEvent { }
}
