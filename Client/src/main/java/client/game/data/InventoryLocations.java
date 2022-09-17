package client.game.data;

public class InventoryLocations {

    public static Integer getInventoryLocation(ItemInfo itemInfo) throws IllegalArgumentException {
        return switch (itemInfo) {
            case TELESCOPE -> 0x803C4C44;
            case BOATS_SAIL -> 0x803C4C45;
            case WIND_WAKER -> 0x803C4C46;
            case GRAPPLING_HOOK -> 0x803C4C47;
            case SPOILS_BAG -> 0x803C4C48;
            case BOOMERANG -> 0x803C4C49;
            case DEKU_LEAF -> 0x803C4C4A;
            case TINGLE_TUNER -> 0x803C4C4B;
            case PICTO_BOX, DELUXE_PICTO_BOX -> 0x803C4C4C;
            case IRON_BOOTS -> 0x803C4C4D;
            case MAGIC_ARMOR -> 0x803C4C4E;
            case BAIT_BAG -> 0x803C4C4F;
            case HEROS_BOW, FIRE_AND_ICE_ARROWS, LIGHT_ARROWS -> 0x803C4C50;
            case BOMBS -> 0x803C4C51;
            case EMPTY_BOTTLE -> 0x803C4C52;
            case DELIVERY_BAG -> 0x803C4C56;
            case HOOKSHOT -> 0x803C4C57;
            case SKULL_HAMMER -> 0x803C4C58;
            default -> throw new IllegalArgumentException("The Provided Item doesn't have an Inventory Slot: " + itemInfo);
        };
    }

    public static Integer getItemOwnedMemoryLocation(ItemInfo itemInfo) throws IllegalArgumentException {
        return switch (itemInfo) {
            case GRAPPLING_HOOK -> 0x803C4C5C;
            case SPOILS_BAG -> 0x803C4C5D;
            case BOOMERANG -> 0x803C4C5E;
            case DEKU_LEAF -> 0x803C4C5F;
            case TINGLE_TUNER -> 0x803C4C60;
            case PICTO_BOX, DELUXE_PICTO_BOX -> 0x803C4C61;
            case IRON_BOOTS -> 0x803C4C62;
            case MAGIC_ARMOR -> 0x803C4C63;
            case BAIT_BAG -> 0x803C4C64;
            case HEROS_BOW, FIRE_AND_ICE_ARROWS, LIGHT_ARROWS -> 0x803C4C65;
            case BOMBS -> 0x803C4C66;
            case DELIVERY_BAG -> 0x803C4C6B;
            case HOOKSHOT -> 0x803C4C6C;
            case SKULL_HAMMER -> 0x803C4C6D;
            default -> throw new IllegalArgumentException("The Provided Inventory Item is not an Inventory Item: " + itemInfo);
        };
    }
}
