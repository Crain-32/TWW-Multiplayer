package crain.client.communication;

import crain.client.communication.external.MultiplayerTrackerApi;
import crain.client.config.GameRoomConfig;
import crain.client.events.ItemFoundEvent;
import crain.client.service.SettingsService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import static crain.client.service.SettingsService.EXTERNAL_TRACKER_WEBSITE;


@Service
public class ExternalService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ExternalService.class);
    private final GameRoomConfig gameRoomConfig;
    private final SettingsService settingsService;
    private MultiplayerTrackerApi multiplayerTrackerApi;

    public ExternalService(GameRoomConfig gameRoomConfig, SettingsService settingsService) {
        this.gameRoomConfig = gameRoomConfig;
        this.settingsService = settingsService;
    }

    @PostConstruct
    public void makeMultiplayerTrackerApi() {
        WebClient client = WebClient.builder().baseUrl(settingsService.getSetting("https://" + settingsService.getSetting(EXTERNAL_TRACKER_WEBSITE))).build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
        this.multiplayerTrackerApi = factory.createClient(MultiplayerTrackerApi.class);
    }

    @Async
    @EventListener(condition = "@communicationState.externalIntegration() && T(constants.WorldType).COOP.equals(#gameRoomConfig?.worldType)")
    public void sendCoopItem(ItemFoundEvent event) {
        try {
            log.debug("Would have sent {} to the Tracker", event.info().getDisplayName());
//            multiplayerTrackerApi.sendItem(createPayload(event));
        } catch (Exception e) {
            // We really don't care if something fails here.
            // So we'll log it for debugging, but nothing else.
//            log.error("Failed to send to Tracker", e);
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

}
