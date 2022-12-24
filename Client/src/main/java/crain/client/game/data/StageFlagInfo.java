package crain.client.game.data;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum StageFlagInfo {
    SEA(0x00, 0x803C4F88),
    SEA_ALT(0x01, 0x803C4FAC),
    FORSAKEN_FORTRESS(0x02, 0x803C4FD0),
    DRAGON_ROOST_CAVERN(0x03, 0x803C4FF4),
    FORBIDDEN_WOODS(0x04, 0x803C5018),
    TOWER_OF_THE_GODS(0x05, 0x803C503C),
    EARTH_TEMPLE(0x06, 0x803C5060),
    WIND_TEMPLE(0x07, 0x803C5084),
    GANONS_TOWER(0x08, 0x803C50A8),
    HYRULE(0x09, 0x803C50CC),
    SHIP_INTERIORS(0x0A, 0x803C50F0),
    HOURS_AND_MISC_PLACES(0x0B, 0x803C5114),
    CAVE_INTERIORS(0x0C, 0x803C5138),
    CONT_CAVE_AND_SHIP_INTERIORS(0x0D, 0x803C515C),
    BLUE_CHUS(0x0E, 0x803C5180),
    TEST_MAPS(0x0F, 0x803C51A4),
    CURRENT(0x10, 0x803C5380);

    private final Byte stageId;
    private final Integer memoryLocation;

    StageFlagInfo(int stageId, Integer memoryLocation) {
        this.stageId = (byte) stageId;
        this.memoryLocation = memoryLocation;
    }

    public static StageFlagInfo fromStageId(Byte stageId) {
        return Arrays.stream(StageFlagInfo.values())
                .filter(stage -> Objects.equals(stage.getStageId(), stageId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No Stage of the Provided ID exists!"));
    }

    public static StageFlagInfo fromMemoryAddress(Integer memoryLocation) {
        return Arrays.stream(StageFlagInfo.values())
                .filter(stage -> Objects.equals(stage.getMemoryLocation(), memoryLocation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("The Memory Address Provided is invalid."));
    }
}
