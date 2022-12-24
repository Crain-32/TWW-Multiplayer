package crain.client.view.events;

import crain.client.events.CreateMemoryAdapterEvent;


public record SettingsChangeEvent(
        CreateMemoryAdapterEvent.MemoryAdapterType memoryAdapterType,
        String consoleIpAddress,
        Boolean enableIntegration) {

}
