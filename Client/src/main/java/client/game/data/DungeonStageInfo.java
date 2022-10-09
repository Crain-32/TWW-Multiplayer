package client.game.data;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * This can likely get merged into StageInfo, I want to iterate on it
 * a bit more before doing that though.
 */
@Slf4j
@Getter
public enum DungeonStageInfo {

    DRAGON_ROOST_CAVERN(3),
    FORBIDDEN_WOODS(4),
    TOWER_OF_THE_GODS(5),
    FORSAKEN_FORTRESS(2),
    EARTH_TEMPLE(6),
    WIND_TEMPLE(7);

    private final Byte stageId;

    DungeonStageInfo(int stageId) {
        this.stageId = (byte) stageId;
    }

    public static DungeonStageInfo fromItem(ItemInfo info) {
        log.trace("Checking Dungeon Stage Info for {}", info.getDisplayName());
        return switch (info) {
            case DRC_SMALL_KEY, DRC_BIG_KEY, DRC_DUNGEON_COMPASS, DRC_DUNGEON_MAP -> DRAGON_ROOST_CAVERN;
            case FW_SMALL_KEY, FW_BIG_KEY, FW_DUNGEON_COMPASS, FW_DUNGEON_MAP -> FORBIDDEN_WOODS;
            case TOTG_SMALL_KEY, TOTG_BIG_KEY, TOTG_DUNGEON_COMPASS, TOTG_DUNGEON_MAP -> TOWER_OF_THE_GODS;
            case FF_DUNGEON_COMPASS, FF_DUNGEON_MAP -> FORSAKEN_FORTRESS;
            case ET_SMALL_KEY, ET_BIG_KEY, ET_DUNGEON_COMPASS, ET_DUNGEON_MAP -> EARTH_TEMPLE;
            case WT_SMALL_KEY, WT_BIG_KEY, WT_DUNGEON_COMPASS, WT_DUNGEON_MAP -> WIND_TEMPLE;
            default -> throw new IllegalArgumentException("The Provided Item is not a Dungeon item: " + info);
        };
    }
}
