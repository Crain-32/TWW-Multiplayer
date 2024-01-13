package crain.client.view.events;

import constants.WorldType;
import crain.client.events.CreateMemoryAdapterEvent;

public record InitialSettingsEvent(
        CreateMemoryAdapterEvent.MemoryAdapterType memoryAdapterType,
        String consoleIpAddress,
        Boolean enableIntegration,
        WorldType worldType,
        String gameRoomName,
        String password,
        Integer worldAmount,
        Integer worldId,
        String playerName,
        String brokerServer) {
}
