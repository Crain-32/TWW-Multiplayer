package crain.client.communication;

import constants.WorldType;
import crain.client.communication.external.MultiplayerTrackerApi;
import crain.client.config.GameRoomConfig;
import crain.client.events.ItemFoundEvent;
import org.slf4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


@Service
public class ExternalService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ExternalService.class);
    private final GameRoomConfig gameRoomConfig;
    //    private final MultiplayerTrackerApi multiplayerTrackerApi;
    private boolean externalIntegrationEnabled = false;

    public ExternalService(GameRoomConfig gameRoomConfig) {
        this.gameRoomConfig = gameRoomConfig;
        //        WebClient client = WebClient.builder().baseUrl(serverBaseUrl).build();
//        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();
//
//        this.multiplayerTrackerApi = factory.createClient(MultiplayerTrackerApi.class);
    }


    @EventListener
    public void toggleIntegration(ToggleIntegrationEvent event) {
        log.debug("Integration Set to: {}", event.enable());
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
                log.trace("Integration does not work for Multiworld Seeds Yet");
                return;
            }
            log.debug("Would have sent {} to the Tracker", event.info().getDisplayName());
//            multiplayerTrackerApi.sendItem(createPayload(event));
        } catch (Exception e) {
            // We really don't care if something fails here.
            // So we'll log it for debugging, but nothing else.
            log.error("Failed to send to Tracker", e);
        }
    }

    private MultiplayerTrackerApi.MultiplayerTrackerPayload createPayload(ItemFoundEvent event) {
        log.trace("Creating a Payload intended for: {} By: {} Item Id: {}",
                gameRoomConfig.getGameRoomName(),
                gameRoomConfig.getPlayerName(),
                event.info().getItemId()
        );
        return MultiplayerTrackerApi.MultiplayerTrackerPayload.builder()
                .gameRoom(gameRoomConfig.getGameRoomName())
                .playerName(gameRoomConfig.getPlayerName())
                .checkName(null) // We'll just leave this as null for now.
                .itemId(event.info().getItemId())
                .build();
    }

    public record ToggleIntegrationEvent(Boolean enable) {
    }
}
