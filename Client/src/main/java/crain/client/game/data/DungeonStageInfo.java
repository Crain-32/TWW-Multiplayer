package crain.client.game.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This can likely get merged into StageInfo, I want to iterate on it
 * a bit more before doing that though.
 */
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DungeonStageInfo implements CheckInterface {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum DRAGON_ROOST_CAVERN{
        FIRST_CHEST(null, null),
        FIRST_LOCKED_DOOR(),
        BREAK_WOODEN_HUB_BARRIER(),
        WATCH_HUB_CUTSCENE(null, null),
        PULL_OUT_HUB_BLOCK(),
        BREAK_POT_ROOM_ROCK(),
        WATER_POT_ROOM_CHEST(),
        BREAK_WOODEN_BOARDED_CHEST_BARRIER(),
        BOARDED_UP_CHEST(),
        HUB_ROOM_ROCK(),
        HUB_LOCKED_DOOR(),
        ACROSS_LAVA_BARRIER(),
        ACROSS_LAVA_CHEST(),
        ACROSS_LAVA_DOOR_SWITCH(),
        BLOCK_ROOM_ROCK(),
        BLOCK_ROOM_WOODEN_BARRIER(),
        BLOCK_ROOM_CHEST_ONE(),
        BLOCK_ROOM_CHEST_TWO(),
        BLOCK_ROOM_LOCKED_DOOR(),
        BIRDS_NEST(),
        BIRDS_NEST_LOCKED_DOOR(),
        DARK_ROOM_CHEST(),
        DARK_ROOM_BARRIER(),
        DARK_ROOM_LANTERN_SWITCH(),
        SECOND_WARP_POT_ROCK(),
        TINGLE_HUB_CHEST(),
        POT_ROOM_CHEST(),
        BOSS_STAIRS_RIGHT_CHEST(),
        BOSS_STAIRS_LEFT_CHEST(),
        THIRD_WARP_POT(),
        ROCK_TO_OUTSIDE(),
        MINIBOSS_DEFEATED(),
        MINIBOSS_CHEST(),
        BOSS_ITEM_RETRIEVED();

        private Byte offset;
        private Byte bitOffset;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum FORBIDDEN_WOODS{
        FIRST_ROOM_CHEST(),
        FIRST_ROOM_BULB(),
        CLIMB_BOKO_BABA_LOWEST_BULB(),
        CLIMB_BOKO_BABA_LOWEST_CHEST(),
        CLIMB_BOKO_BABA_EXIT_DOOR_BULB(),
        CLIMB_BOKO_BABA_INTRA_DUNGEON_WARP_POT(),
        CLIMB_BOKO_BABA_UPPER_BULB(),
        CLIMB_BOKO_BABA_UPPER_CHEST(),
        KILL_BOKO_BABA(),
        CHEST_IN_MIDDLE_OF_TREE(),
        DOOR_BULB_AFTER_THAT(),
        DOOR_BULB_TO_HUB(),
        HUB_CUTSCENE(),
        HUB_LOCKED_DOOR(),
        HUB_WARP_POT(),
        DOOR_TO_VINE_MAZE_BULB(),
        BOSS_CORRIDOR_BULBS(),
        CUT_DOWN_HUB_HOUSE(),
        VINE_MAZE_LEFT_CHEST(),
        VINE_MAZE_RIGHT_CHEST(),
        CHEST_ABOVE_MINIBOSS(),
        MINIBOSS_CHEST(),
        BULBS_OUTSIDE_MINIBOSS(),
        CHEST_ACROSS_VINES(),
        CHEST_ACROSS_RED_FLOWER(),
        BASEMENT_WOODEN_BARRIER(),
        TINGLE_CHEST(),
        BULB_CHEST_IN_TREE(),
        BIG_KEY_CHEST(),
        DOUBLE_MOTHULA_CHEST(),
        BOSS_ITEM_RECEIVED();


        private Byte offset;
        private Byte bitOffset;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum TOWER_OF_THE_GODS{
        FIRST_CRACKED_WALL(),
        SECOND_CRACKED_WALL(),
        THIRD_CRACKED_WALL(),
        FIRST_LOCKED_DOOR(),
        LEFT_SIDE_STATUE_PLACED(),
        LIGHT_TWO_TORCHES_CHEST(),
        SKULL_EYE_CHEST(),
        SHOOT_EYE_CHEST(),
        BOMBABLE_WALL_CHEST(),
        RIGHT_SIDE_STATUE(),
        HOP_ACROSS_BOXES_CHEST(),
        ELEVATOR_ENABLED(),
        WARP_POT_OPENED(),
        FIRST_ARMOS_EYE_SHOT(),
        FIRST_ARMOS_CHEST(),
        FIRST_STATUE_CALLED(), // Not 100% sure on this one
        FIRST_STATUE_PLACED(),
        STONE_TABLET_ITEM_RECEIVED(),
        SECOND_STATUE_EYE_SWITCH(),
        MINIBOSS_CHEST(),
        SECOND_STATUE_CALLED(),
        SECOND_STATUE_PLACED(),
        BOMBABLE_WALL_ARMOS_CHEST(),
        FLOATING_PLATFORMS_SWITCH_ABOVE_DOOR(),
        FLOATING_PLATFORMS_LOW_EYE_SWITCH(),
        FLOATING_PLATFORMS_LOWER_CHEST(),
        FLOATING_PLATFORMS_HIGH_EYE_SWITCH(),
        FLOATING_PLATFORMS_HIGH_CHEST(),
        SECOND_LOCKED_DOOR(),
        THIRD_STATUE_CALLED(),
        THIRD_STATUE_PLACED(),
        BIG_KEY_CHEST(),
        BOSS_ITEM();


        private Byte offset;
        private Byte bitOffset;

    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum FORSAKEN_FORTRESS {
        PHANTOM_GANON_CHEST(),
        WARP_POT(),
        BOKOBLIN_CHEST(),
        CHEST_ON_BED(),
        HIGH_JAIL_CHEST(),
        LOWER_JAIL_CHEST(),
        SWITCH_BEFORE_HELMAROC(),
        BOSS_ITEM();

        private Byte offset;
        private Byte bitOffset;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum EARTH_TEMPLE {
        INTRA_DUNGEON_WARP_POT(),
        UNLOCK_FIRST_DOOR(),
        TRANSPARENT_CHEST_IN_CHU_ROOM(),
        FIRST_WARP_POT(),
        CHU_ROOM_JOY_PENDANT(),
        MAIN_ROOM_HAMMER_SWITCH(),
        MAIN_ROOM_SUN_SWITCH(),
        RIGHT_SIDE_TRANSPARENT_CHEST(),
        CHEST_BEHIND_DESTRUCTIBLE_WALLS(),
        LEFT_SIDE_LOCKED_DOOR(),
        BANISTER_HIDING_LIGHT(),
        LARGE_BLOCK_IN_LEFT_SIDE(),
        FIRST_BLOCK(),
        SECOND_BLOCK(),
        THIRD_BLOCK(),
        THREE_BLOCK_ROOM_CHEST(),
        THREE_BLOCK_ROOM_WARP_POT(),
        HIGHEST_ROOM_DESTRUCTIBLE_WALL(),
        HIGHEST_ROOM_ELEPHANT_STATUE(),
        SECOND_CRYPT_ITEM(),
        MINIBOSS_ITEM(),
        TINGLE_CHEST(),
        FIRST_SONG_STONE(),
        LOWER_HUB_RIGHT_STATUE(),
        LOWER_HUB_LEFT_STATUE(),
        STATUE_ON_SMALL_BLOCK(),
        FLOORMASTERS_FIRST_CHEST(),
        KILL_ALL_FLOORMASTERS_CHEST(),
        LOWER_HUB_LEFT_SIDE_JOY_PENDANT(),
        LOWER_HUB_LEFT_SIDE_HAMMER_SWITCH(),
        LOWER_HUB_TRANSITION_ROOM_ELEPHANT_STATUE(),
        THIRD_CRYPT_CHEST(),
        SECOND_SONG_STONE(),
        THIRD_WARP_POT(),
        BASEMENT_RIGHT_TRANSPARENT_CHEST(),
        BASEMENT_LEFT_TRANSPARENT_CHEST(),
        BIG_KEY_CHEST(),
        THREE_STALFOS_ROOM_CHEST(),
        BOSS_ITEM();

        private Byte offset;
        private Byte bitOffset;
    }
    public enum WIND_TEMPLE {}

    public static Byte stageIdFromItem(ItemInfo info) {
        log.trace("Checking Dungeon Stage Info for {}", info.getDisplayName());
        return switch (info) {
            case DRC_SMALL_KEY, DRC_BIG_KEY, DRC_DUNGEON_COMPASS, DRC_DUNGEON_MAP -> (byte) 3;
            case FW_SMALL_KEY, FW_BIG_KEY, FW_DUNGEON_COMPASS, FW_DUNGEON_MAP -> (byte) 4;
            case TOTG_SMALL_KEY, TOTG_BIG_KEY, TOTG_DUNGEON_COMPASS, TOTG_DUNGEON_MAP -> (byte) 5;
            case FF_DUNGEON_COMPASS, FF_DUNGEON_MAP -> (byte) 2;
            case ET_SMALL_KEY, ET_BIG_KEY, ET_DUNGEON_COMPASS, ET_DUNGEON_MAP -> (byte) 6;
            case WT_SMALL_KEY, WT_BIG_KEY, WT_DUNGEON_COMPASS, WT_DUNGEON_MAP -> (byte) 7;
            default -> throw new IllegalArgumentException("The Provided Item is not a Dungeon item: " + info);
        };
    }

    @Override
    public String getCheckName() {
        return null;
    }
}
