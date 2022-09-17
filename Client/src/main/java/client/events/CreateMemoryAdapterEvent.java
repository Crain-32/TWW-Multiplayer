package client.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateMemoryAdapterEvent {

    private final MemoryAdapterType memoryAdapterType;

    public enum MemoryAdapterType {
        DOLPHIN,
        NINTENDONT
    }
}
