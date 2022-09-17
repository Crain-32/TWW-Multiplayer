package client.view.events;

import client.events.CreateMemoryAdapterEvent;


public record SettingsChangeEvent(
        CreateMemoryAdapterEvent.MemoryAdapterType memoryAdapterType,
        String consoleIpAddress,
        Boolean enableIntegration) {

}
