package crain.client.communication;

import crain.client.view.events.ServerConnectEvent;
import crain.client.view.events.ServerDisconnectEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommunicationState {

    private boolean connectedToBrokerServer;
    private boolean connectedToTrackerServer;
    private boolean externalIntegration;

    private boolean connectedToGame;


    public boolean connectedToMultiplayerServer() {
        return this.connectedToBrokerServer;
    }

    public boolean trackerServer() {
        return this.connectedToTrackerServer;
    }

    public boolean externalIntegration() {
        return this.externalIntegration;
    }

    public boolean isConnectedToGame() {
        return this.connectedToGame;
    }

    @EventListener(ServerConnectEvent.class)
    public void setConnectedToBrokerServer() {
        log.trace("Current Connected State: {}", this.connectedToBrokerServer);
        this.connectedToBrokerServer = true;
    }

    @EventListener(ServerDisconnectEvent.class)
    public void setDisconnectedToBrokerServer() {
        log.trace("Current Connected State: {}", this.connectedToBrokerServer);
        this.connectedToBrokerServer = false;
    }


    @EventListener(condition = "event?.enable() != null") // No need to async this
    public void toggleIntegration(ToggleIntegrationEvent event) {
        log.trace("Integration Set to: {}", event.enable());
        this.externalIntegration = event.enable();
    }

    @EventListener(condition = "event?.enable() != null") // No need to async this
    public void toggleGameConnection(ToggleGameConnectionEvent event) {
        log.trace("Integration Set to: {}", event.enable());
        this.connectedToGame = event.enable();
    }


    public record ToggleIntegrationEvent(Boolean enable) {
    }

    public record ToggleGameConnectionEvent(Boolean enable) {
    }
}
