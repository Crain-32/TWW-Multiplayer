package crain.client.view.events;

import crain.client.events.CreateMemoryAdapterEvent;


public record SettingsPageChangeEvent(
        CreateMemoryAdapterEvent.MemoryAdapterType memoryAdapterType,
        String consoleIpAddress,
        Boolean enableIntegration
) {
}
