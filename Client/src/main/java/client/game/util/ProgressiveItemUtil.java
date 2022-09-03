package client.game.util;

import client.game.data.ItemInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressiveItemUtil {

    public static Integer getProgressiveAddress(ItemInfo info) {
        return switch (info) {
            case HEROS_SWORD, MASTER_SWORD_ONE, MASTER_SWORD_HALF, MASTER_SWORD_FULL ->  0x803C4C16;
            case HEROS_BOW, FIRE_AND_ICE_ARROWS, LIGHT_ARROWS -> 0x803C4C50;
            case HEROS_SHIELD, MIRROR_SHIELD -> 0x803C4C17;
            case PICTO_BOX, DELUXE_PICTO_BOX -> 0x803C4C4C;
            default -> throw new IllegalArgumentException("The Provided Item ID cannot map");
        };
    }

    public static Integer getProgressiveFlagAddress(ItemInfo info) {
        return switch (info) {
            case HEROS_SWORD, MASTER_SWORD_ONE, MASTER_SWORD_HALF, MASTER_SWORD_FULL ->  0x803C4CBC;
            case HEROS_BOW, FIRE_AND_ICE_ARROWS, LIGHT_ARROWS -> 0x803C4C65;
            case HEROS_SHIELD, MIRROR_SHIELD -> 0x803C4CBD;
            case PICTO_BOX, DELUXE_PICTO_BOX -> 0x803C4C61;
            default -> throw new IllegalArgumentException("The Provided Item ID cannot map");
        };
    }

    /**
     * Returns the 0-based index of the Progression. Required to handle the Owned-bit flag.
     */
    public static Integer getProgressiveIndex(ItemInfo info) {
        return switch (info) {
            case HEROS_BOW, HEROS_SHIELD, HEROS_SWORD, PICTO_BOX -> 0;
            case FIRE_AND_ICE_ARROWS, MIRROR_SHIELD, MASTER_SWORD_ONE, DELUXE_PICTO_BOX-> 1;
            case LIGHT_ARROWS, MASTER_SWORD_HALF -> 2;
            case MASTER_SWORD_FULL -> 3;
            default -> throw new IllegalArgumentException("The Provided Item Id Cannot map");
        };
    }

    public static ItemInfo getNextProgression(ItemInfo info) {
        return switch (info) {
            case HEROS_BOW -> ItemInfo.FIRE_AND_ICE_ARROWS;
            case FIRE_AND_ICE_ARROWS -> ItemInfo.LIGHT_ARROWS;
            case HEROS_SHIELD -> ItemInfo.MIRROR_SHIELD;
            case HEROS_SWORD -> ItemInfo.MASTER_SWORD_ONE;
            case MASTER_SWORD_ONE -> ItemInfo.MASTER_SWORD_HALF;
            case MASTER_SWORD_HALF -> ItemInfo.MASTER_SWORD_FULL;
            case PICTO_BOX -> ItemInfo.DELUXE_PICTO_BOX;
            case LIGHT_ARROWS, MIRROR_SHIELD, MASTER_SWORD_FULL, DELUXE_PICTO_BOX -> ItemInfo.INVALID_ID;
            default -> throw new IllegalArgumentException("The Provided ItemInfo is not Progressive");
        };
    }

    public static ItemInfo getPreviousProgression(ItemInfo info) {
        return switch (info) {
            case LIGHT_ARROWS -> ItemInfo.FIRE_AND_ICE_ARROWS;
            case FIRE_AND_ICE_ARROWS -> ItemInfo.HEROS_BOW;
            case DELUXE_PICTO_BOX -> ItemInfo.PICTO_BOX;
            case MIRROR_SHIELD -> ItemInfo.HEROS_SHIELD;
            case MASTER_SWORD_FULL -> ItemInfo.MASTER_SWORD_HALF;
            case MASTER_SWORD_HALF -> ItemInfo.MASTER_SWORD_ONE;
            case MASTER_SWORD_ONE -> ItemInfo.HEROS_SWORD;
            case HEROS_BOW, PICTO_BOX, HEROS_SHIELD, HEROS_SWORD -> ItemInfo.INVALID_ID;
            default -> throw new IllegalArgumentException("The Provided ItemInfo is not Progressive");
        };
    }
}
