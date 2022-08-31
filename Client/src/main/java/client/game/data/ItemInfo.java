package client.game.data;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum ItemInfo {

    HEART(0x00, "Heart (Pickup)f", -1),
    GREEN_RUPEE(0x01, "Green Rupee", -1),
    BLUE_RUPEE(0x02, "Blue Rupee", -1),
    YELLOW_RUPEE(0x03, "Yellow Rupee", -1),
    RED_RUPEE(0x04, "Red Rupee", -1),
    PURPLE_RUPEE(0x05, "Purple Rupee", -1),
    ORANGE_RUPEE(0x06, "Orange Rupee", -1),
    PIECE_OF_HEART(0x07, "Piece of Heart", -1),
    HEART_CONTAINER(0x08, "Heart Container", -1),
    SMALL_MAGIC_JAR(0x09, "Small Magic Jar", -1),
    LARGE_MAGIC_JAR(0x0A, "Large Magic Jar", -1),
    FIVE_BOMBS(0x0B, "5 Bombs", -1),
    TEN_BOMBS(0x0C, "10 Bombs", -1),
    TWENTY_BOMBS(0x0D, "20 Bombs", -1),
    THIRTY_BOMBS(0x0E, "30 Bombs", -1),
    SILVER_RUPEE(0x0F, "Silver Rupee", -1),
    TEN_ARROWS(0x10, "10 Arrows", -1),
    TWENTY_ARROWS(0x011, "20 Arrows", -1),
    THIRTY_ARROWS(0x012, "30 Arrows", -1),
    DRC_SMALL_KEY(0x13, "DRC Small Key", 4), // Dungeon Items are going to be handled in classes
    DRC_BIG_KEY(0x14, "DRC Big Key", 1),
    SMALL_KEY(0x15, "Generic Small Key", -1),
    FAIRY(0x16, "Fairy", -1),
    YELLOW_RUPEE_JOKE(0x1A, "Yellow Joke Rupee", -1),
    DRC_DUNGEON_MAP(0x1B, "DRC Dungeon Map", 1),
    DRC_DUNGEON_COMPASS(0x1C, "DRC Dungeon Compass", 1),
    FW_SMALL_KEY(0x1D, "FW Small Key", 2),
    ZELDA_HEARTS(0x1E, "Zelda Hearts", -1),
    JOY_PENDANT(0x1F, "Joy Pendant", -1), // TODO, Support Sharing Spoils Items
    TELESCOPE(0x20, "Telescope", 1),
    TINGLE_TUNER(0x21, "Tingle Tuner", 1),
    WIND_WAKER(0x22, "Wind Waker", 1),
    PICTO_BOX(0x023, "Picto Box", 1),
    SPOILS_BAG(0x24, "Spoils Bag", 1),
    GRAPPLING_HOOK(0x25, "Grappling Hook", 1),
    DELUXE_PICTO_BOX(0x26, "Deluxe Picto Box", 1),
    HEROS_BOW(0x27, "Hero's Bow", 1),
    POWER_BRACELETS(0x28, "Power Bracelets", 1), // Bit Flag, Should be Handled fo
    IRON_BOOTS(0x29, "Iron Boots", 1),
    MAGIC_ARMOR(0x2A, "Magic Armor", 1),
    BAIT_BAG(0x2C, "Bait Bag", 1),
    BOOMERANG(0x2D, "Boomerang", 1),
    HOOKSHOT(0x2F, "Hookshot", 1),
    DELIVERY_BAG(0x30, "Delivery Bag", 1),
    BOMBS(0x31, "Bombs", 1),
    HEROS_CLOTHES(0x32, "Hero's Clothes", 1),
    SKULL_HAMMER(0x33, "Skull Hammer", 1),
    DEKU_LEAF(0x34, "Deku Leaf", 1),
    FIRE_AND_ICE_ARROWS(0x35, "Fire and Ice Arrows", 1),
    LIGHT_ARROWS(0x36, "Light Arrows", 1),
    HEROS_NEW_CLOTHES(0x37, "Hero's New Clothes", 1),
    HEROS_SWORD(0x38, "Heros' Sword", 1),
    MASTER_SWORD_ONE(0x39, "Master Sword (Powerless)", 1),
    MASTER_SWORD_HALF(0x3A, "Master Sword (Half Power)", 1),
    HEROS_SHIELD(0x3B, "Hero's Shield", 1),
    MIRROR_SHIELD(0x3C, "Mirror Shield", 1),
    MASTER_SWORD_FULL(0x3D, "Master Sword (Full Power)", 1),
    PIECE_OF_HEART_ALT(0x3F, "Piece of Heart (Alternate Message)", 1),
    FW_BIG_KEY(0x40, "FW Big Key", 1),
    FW_DUNGEON_MAP(0x41, "FW Dungeon Map", 1),
    PIRATES_CHARM(0x42, "Pirate's Charm", 1),
    HEROS_CHARM(0x43, "Hero's Charm", 1),
    SKULL_NECKLACE(0x45, "Skull Necklace", -1),
    BOKO_BABA_SEED(0x46, "Boko Baba Seed", -1),
    GOLDEN_FEATHER(0x47, "Golden Feather", -1),
    KNIGHTS_CREST(0x48, "Knight's Crest", -1),
    RED_CHU_JELLY(0x49, "Red Chu Jelly", -1),
    GREEN_CHU_JELLY(0x4A, "Green Chu Jelly", -1),
    BLUE_CHU_JELLY(0x4B, "Blue Chu Jelly", -1),
    GENERIC_DUNGEON_MAP(0x4C, "Dungeon Map", -1),
    GENERIC_COMPASS(0x4D, "Compass", -1),
    GENERIC_BIG_KEY(0x4E, "Big Key", -1),
    EMPTY_BOTTLE(0x50, "Empty Bottle", 4),
    RED_POTION(0x51, "Red Potion", -1),
    GREEN_POTION(0x52, "Green Potion", -1),
    BLUE_POTION(0x53, "Blue Potion", -1),
    ELIXIR_SOUP_HALF(0x54, "Elixir Soup (1/2)", -1),
    ELIXIR_SOUP(0x55, "Elixir Soup", -1),
    BOTTLED_WATER(0x56, "Bottled Water", -1),
    FAIRY_IN_BOTTLE(0x57, "Fairy in Bottle", -1),
    FOREST_FIREFLY(0x58, "Forest Firefly", -1),
    FOREST_WATER(0x59, "Forest Water", -1),
    FW_DUNGEON_COMPASS(0x5A, "FW Dungeon Compass", 1),
    TOTG_SMALL_KEY(0x5B, "TotG Small Key", 2),
    TOTG_BIG_KEY(0x5C, "TotG Big Key", 1),
    TOTG_DUNGEON_MAP(0x5D, "TotG Dungeon Map", 1),
    TOTG_DUNGEON_COMPASS(0x5E, "TotG Dungeon Compass", 1),
    FF_DUNGEON_MAP(0x5F, "FF Dungeon Map", 1),
    FF_DUNGEON_COMPASS(0x60, "FF Dungeon Compass", 1),
    TRIFORCE_SHARD_ONE(0x61, "Triforce Shard 1", 1),
    TRIFORCE_SHARD_TWO(0x62, "Triforce Shard 2", 1),
    TRIFORCE_SHARD_THREE(0x63, "Triforce Shard 3", 1),
    TRIFORCE_SHARD_FOUR(0x64, "Triforce Shard 4", 1),
    TRIFORCE_SHARD_FIVE(0x65, "Triforce Shard 5", 1),
    TRIFORCE_SHARD_SIX(0x66, "Triforce Shard 6", 1),
    TRIFORCE_SHARD_SEVEN(0x67, "Triforce Shard 7", 1),
    TRIFORCE_SHARD_EIGHT(0x68, "Triforce Shard 8", 1),
    NAYRUS_PEARL(0x69, "Nayru's Pearl", 1),
    DINS_PEARL(0x6A, "Din's Pearl", 1),
    FARORES_PEARL(0x6B, "Farore's Pearl", 1),
    WINDS_REQUIEM(0x6D, "Wind's Requiem", 1),
    BALLAD_OF_GALES(0x6E, "Ballad of Gales", 1),
    COMMAND_MELODY(0x6F, "Command Melody", 1),
    EARTH_GODS_LYRIC(0x70, "Earth God's Lyric", 1),
    WIND_GODS_ARIA(0x71, "Wind God's Aria", 1),
    SONG_OF_PASSING(0x72, "Song of Passing", 1),
    ET_SMALL_KEY(0x73, "ET Small Key", 3),
    ET_BIG_KEY(0x74, "ET Big Key", 1),
    ET_DUNGEON_MAP(0x75, "ET Dungeon Map", 1),
    ET_DUNGEON_COMPASS(0x76, "ET Dungeon Compass", 1),
    WT_SMALL_KEY(0x77, "WT Small Key", 2),
    BOATS_SAIL(0x78, "Boat's Sail", 1),
    TRANSLATED_CHART_ONE(0x79, "Triforce Chart 1 got deciphered", 1),
    TRANSLATED_CHART_TWO(0x7A, "Triforce Chart 2 got deciphered", 1),
    TRANSLATED_CHART_THREE(0x7B, "Triforce Chart 3 got deciphered", 1),
    TRANSLATED_CHART_FOUR(0x7C, "Triforce Chart 4 got deciphered", 1),
    TRANSLATED_CHART_FIVE(0x7D, "Triforce Chart 5 got deciphered", 1),
    TRANSLATED_CHART_SIX(0x7E, "Triforce Chart 6 got deciphered", 1),
    TRANSLATED_CHART_SEVEN(0x7F, "Triforce Chart 7 got deciphered", 1),
    TRANSLATED_CHART_EIGHT(0x80, "Triforce Chart 8 got deciphered", 1),
    WT_BIG_KEY(0x81, "WT Big Key", 1),
    ALL_PURPOSE_BAIT(0x82, "All-Purpose Bait", -1),
    HYOI_PEAR(0x83, "Hyoi Pear", -1),
    WT_DUNGEON_MAP(0x84, "WT Dungeon Map", 1),
    WT_DUNGEON_COMPASS(0x85, "WT Dungeon Compass", 1),
    TOWN_FLOWER(0x8C, "Town Flower", -1),
    SEA_FLOWER(0x8D, "Sea Flower", -1),
    EXOTIC_FLOWER(0x8E, "Exotic Flower", -1),
    HEROS_FLAG(0x8F, "Hero's Flag", -1),
    BIG_CATCH_FLAG(0x90, "Big Catch Flag", -1),
    BIG_SALE_FLAG(0x91, "Big Sale Flag", -1),
    PINWHEEL(0x92, "Pinwheel ", -1),
    SICKLE_MOON_FLAG(0x93, "Sickle Moon Flag", -1),
    SKULL_TOWER_IDOL(0x94, "Skull Tower Idol", -1),
    FOUNTAIN_IDOL(0x95, "Fountain Idol", -1),
    POSTMAN_STATUE(0x96, "Postman Statue", -1),
    SHOP_GURU_STATUE(0x97, "Shop Guru Statue", -1),
    FATHERS_LETTER(0x98, "Father's Letter", 1),
    NOTE_TO_MOM(0x99, "Note to Mom", 1),
    MAGGIES_LETTER(0x9A, "Maggie's Letter", 1),
    MOBLINS_LETTER(0x9B, "Moblin's Letter", 1),
    CABANA_DEED(0x9C, "Cabana Deed", 1),
    COMPLIMENTARY_ID(0x9D, "Complimentary ID", 1),
    FILL_UP_COUPON(0x9E, "Fill-Up Coupon", 1),
    LEGENDARY_PICTOGRAPH(0x9F, "Legendary Pictograph", -1),
    DRAGON_TINGLE_STATUE(0xA3, "Dragon Tingle Statue", 1),
    FORBIDDEN_TINGLE_STATUE(0xA4, "Forbidden Tingle Statue", 1),
    GODDESS_TINGLE_STATUE(0xA5, "Goddess Tingle Statue", 1),
    EARTH_TINGLE_STATUE(0xA6, "Earth Tingle Statue", 1),
    WIND_TINGLE_STATUE(0xA7, "Wind Tingle Statue", 1),
    HURRICANE_SPIN(0xAA, "Hurricane Spin", 1),
    THOUSAND_RUPEE_WALLET(0xAB, "1000 Rupee Wallet", 1),
    FIVE_THOU_RUPEE_WALLET(0xAC, "5000 Rupee Wallet", 1),
    SIXTY_BOMB_BAG(0xAD, "60 Bomb Bag", 1),
    MAX_BOMB_BAG(0xAE, "99 Bomb Bag", 1),
    SIXTY_QUIVER(0xAF, "60 Arrow Quiver", 1),
    MAX_QUIVER(0xB0, "99 Arrow Quiver", 1),
    MAGIC_METER_UPGRADE(0xB2, "Magic Meter Upgrade", 1),
    FIRST_TINGLE_REWARD(0xB3, "Reward for Finding 1 Tingle Statues", 1),
    SECOND_TINGLE_REWARD(0xB4, "Reward for Finding 2 Tingle Statues", 1),
    THIRD_TINGLE_REWARD(0xB5, "Reward for Finding 3 Tingle Statues", 1),
    FOURTH_TINGLE_REWARD(0xB6, "Reward for Finding 4 Tingle Statues", 1),
    FIFTH_TINGLE_REWARD(0xB7, "Reward for Finding 5 Tingle Statues", 1),
    TINGLE_RUPEE(0xB8, "Tingle Rupee", 1),
    SUBMARINE_CHART(0xC2, "Submarine Chart", 1),
    BEEDLES_CHART(0xC3, "Beedle's Chart", 1),
    PLATFORM_CHART(0xC4, "Platform Chart", 1),
    LIGHT_RING_CHART(0xC5, "Light Ring Chart", 1),
    SECRET_CAVE_CHART(0xC6, "Secret Cave Chart", 1),
    SEA_HEARTS_CHART(0xC7, "Sea Hearts Chart", 1),
    ISLAND_HEARTS_CHART(0xC8, "Island Hearts Chart", 1),
    GREAT_FAIRY_CHART(0xC9, "Great Fairy Chart", 1),
    OCTO_CHART(0xCA, "Octo Chart", 1),
    INCREDIBLE_CHART(0xCB, "IN-credible Chart", 1),
    TREASURE_CHART_SEVEN(0xCC, "Treasure Chart 7", 1),
    TREASURE_CHART_TWENTY_SEVEN(0xCD, "Treasure Chart 27", 1),
    TREASURE_CHART_TWENTY_ONE(0xCE, "Treasure Chart 21", 1),
    TREASURE_CHART_THIRTEEN(0xCF, "Treasure Chart 13", 1),
    TREASURE_CHART_THIRTY_TWO(0xD0, "Treasure Chart 32", 1),
    TREASURE_CHART_NINETEEN(0xD1, "Treasure Chart 19", 1),
    TREASURE_CHART_FORTY_ONE(0xD2, "Treasure Chart 41", 1),
    TREASURE_CHART_TWENTY_SIX(0xD3, "Treasure Chart 26", 1),
    TREASURE_CHART_EIGHT(0xD4, "Treasure Chart 8", 1),
    TREASURE_CHART_THIRTY_SEVEN(0xD5, "Treasure Chart 37", 1),
    TREASURE_CHART_TWENTY_FIVE(0xD6, "Treasure Chart 25", 1),
    TREASURE_CHART_SEVENTEEN(0xD7, "Treasure Chart 17", 1),
    TREASURE_CHART_THIRTY_SIX(0xD8, "Treasure Chart 36", 1),
    TREASURE_CHART_TWENTY_TWO(0xD9, "Treasure Chart 22", 1),
    TREASURE_CHART_NINE(0xDA, "Treasure Chart 9", 1),
    GHOST_SHIP_CHART(0xDB, "Ghost Ship Chart", 1),
    TINGLES_CHART(0xDC, "Tingle's Chart", 1),
    TREASURE_CHART_FOURTEEN(0xDD, "Treasure Chart 14", 1),
    TREASURE_CHART_TEN(0xDE, "Treasure Chart 10", 1),
    TREASURE_CHART_FORTY(0xDF, "Treasure Chart 40", 1),
    TREASURE_CHART_THREE(0xE0, "Treasure Chart 3", 1),
    TREASURE_CHART_FOUR(0xE1, "Treasure Chart 4", 1),
    TREASURE_CHART_TWENTY_EIGHT(0xE2, "Treasure Chart 28", 1),
    TREASURE_CHART_SIXTEEN(0xE3, "Treasure Chart 16", 1),
    TREASURE_CHART_EIGHTEEN(0xE4, "Treasure Chart 18", 1),
    TREASURE_CHART_THIRTY_FOUR(0xE5, "Treasure Chart 34", 1),
    TREASURE_CHART_TWENTY_NINE(0xE6, "Treasure Chart 29", 1),
    TREASURE_CHART_ONE(0xE7, "Treasure Chart 1", 1),
    TREASURE_CHART_THIRTY_FIVE(0xE8, "Treasure Chart 35", 1),
    TREASURE_CHART_TWELVE(0xE9, "Treasure Chart 12", 1),
    TREASURE_CHART_SIX(0xEA, "Treasure Chart 6", 1),
    TREASURE_CHART_TWENTY_FOUR(0xEB, "Treasure Chart 24", 1),
    TREASURE_CHART_THIRTY_NINE(0xEC, "Treasure Chart 39", 1),
    TREASURE_CHART_THIRTY_EIGHT(0xED, "Treasure Chart 38", 1),
    TREASURE_CHART_TWO(0xEE, "Treasure Chart 2", 1),
    TREASURE_CHART_THIRTY_THREE(0xEF, "Treasure Chart 33", 1),
    TREASURE_CHART_THIRTY_ONE(0xF0, "Treasure Chart 31", 1),
    TREASURE_CHART_TWENTY_THREE(0xF1, "Treasure Chart 23", 1),
    TREASURE_CHART_FIVE(0xF2, "Treasure Chart 5", 1),
    TREASURE_CHART_TWENTY(0xF3, "Treasure Chart 20", 1),
    TREASURE_CHART_THIRTY(0xF4, "Treasure Chart 30", 1),
    TREASURE_CHART_FIFTEEN(0xF5, "Treasure Chart 15", 1),
    TREASURE_CHART_ELEVEN(0xF6, "Treasure Chart 11", 1),
    TRIFORCE_CHART_EIGHT(0xF7, "Triforce Chart 8", 1),
    TRIFORCE_CHART_SEVEN(0xF8, "Triforce Chart 7", 1),
    TRIFORCE_CHART_SIX(0xF9, "Triforce Chart 6", 1),
    TRIFORCE_CHART_FIVE(0xFA, "Triforce Chart 5", 1),
    TRIFORCE_CHART_FOUR(0xFB, "Triforce Chart 4", 1),
    TRIFORCE_CHART_THREE(0xFC, "Triforce Chart 3", 1),

    TRIFORCE_CHART_TWO(0xFD, "Triforce Chart 2", 1),
    TRIFORCE_CHART_ONE(0xFE, "Triforce Chart 1", 1),
    INVALID_ID(0xFF, "Invalid Id", -1);

    private final Byte itemId;
    private final String displayName;
    private final Integer maxAmount;

    ItemInfo(int itemId, String displayName, int maxAmount) {
        this.itemId = (byte) itemId;
        this.displayName = displayName;
        this.maxAmount = maxAmount;
    }

    public static ItemInfo getInfoByItemId(Integer itemId) {
        return getInfoByItemId(itemId.byteValue());
    }

    public ItemInfo getInfoByItemId(Byte itemId) {
        return Arrays.stream(ItemInfo.values())
                .filter(info -> Objects.equals(info.getItemId(), itemId))
                .findFirst().orElse(ItemInfo.INVALID_ID);
    }
}
