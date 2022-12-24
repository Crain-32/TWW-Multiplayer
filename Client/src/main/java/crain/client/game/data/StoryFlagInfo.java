package crain.client.game.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum StoryFlagInfo implements CheckInterface {

    DINS_PEARL(0x803C4CC7, 1),
    FARORES_PEARL(0x803C4CC7, 2),
    NAYRUS_PEARL(0x803C4CC7, 0),
    RAISE_TOTG(0x803C524A, 6),
    // Hero's Charm and Hurricane Spin
    // technically shouldn't be here, but eh.
    HEROS_CHARM(0x803C4CC0, 1),
    HURRICANE_SPIN(0x803C5295, 0);

    private final Integer memoryAddress;
    private final Integer bitIndex;

    public static StoryFlagInfo fromMemoryAddressAndBitIndex(Integer memoryLocation, Integer bitOffset) {
        return Arrays.stream(StoryFlagInfo.values())
                .filter(event -> Objects.equals(event.getMemoryAddress(), memoryLocation))
                .filter(event -> Objects.equals(event.getBitIndex(), bitOffset))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("There is no Event at " + memoryLocation + " for offset " + bitOffset));
    }

    @Override
    public String getCheckName() {
        return null;
    }
}
