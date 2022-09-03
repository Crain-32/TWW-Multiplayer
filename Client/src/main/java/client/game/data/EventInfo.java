package client.game.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventInfo {

    DINS_PEARL(0x803C4CC7, 1),
    FARORES_PEARL(0x803C4CC7, 2),
    NAYRUS_PEARL(0x803C4CC7, 0),
    RAISE_TOTG(0x803C524A, 6),
    // Hero's Charm and Hurricane Spin
    // technically shouldn't be here, but eh.
    HEROS_CHARM(0x803C4CC0, 1),
    HURRICANE_SPIN(0x803C5295, 0);

    private final Integer consoleAddress;
    private final Integer bitIndex;
}
