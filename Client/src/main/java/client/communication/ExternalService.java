package client.communication;

import client.communication.external.MultiplayerTrackerApi;
import client.config.GameRoomConfig;
import client.events.ItemFoundEvent;
import constants.WorldType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalService {

    private final GameRoomConfig gameRoomConfig;
    private final MultiplayerTrackerApi multiplayerTrackerApi;
    private boolean externalIntegrationEnabled = false;


    @EventListener
    public void toggleIntegration(ToggleIntegrationEvent event) {
        log.debug("Integration Set to: " + event.enable());
        externalIntegrationEnabled = event.enable();
    }

    @EventListener
    public void sendItemFound(ItemFoundEvent event) {
        try {
            if (!externalIntegrationEnabled) {
                log.trace("Integration is not enabled");
                return;
            }
            if (gameRoomConfig.getWorldType() == WorldType.MULTIWORLD) {
                log.trace("Integration done not work for Multiworld Seeds Yet");
                return;
            }
            multiplayerTrackerApi.sendItem(createPayload(event));
        } catch (Exception e) {
            // We really don't care if something fails here.
            // So we'll log it for debugging, but nothing else.
            log.debug("Failed to send to Tracker", e);
        }
    }

    private MultiplayerTrackerApi.MultiplayerTrackerPayload createPayload(ItemFoundEvent event) {
        if (log.isTraceEnabled()) {
            log.trace("Creating a Payload intended for: %s By: %s Item Id: %s"
                    .formatted(
                            gameRoomConfig.getGameRoomName(),
                            gameRoomConfig.getPlayerName(),
                            event.getInfo().getItemId()
                    )
            );
        }
        return MultiplayerTrackerApi.MultiplayerTrackerPayload.builder()
                .gameRoom(gameRoomConfig.getGameRoomName())
                .playerName(gameRoomConfig.getPlayerName())
                .checkName(null) // We'll just leave this as null for now.
                .itemId(event.getInfo().getItemId())
                .build();
    }

    public record ToggleIntegrationEvent(Boolean enable) {
    }
}
