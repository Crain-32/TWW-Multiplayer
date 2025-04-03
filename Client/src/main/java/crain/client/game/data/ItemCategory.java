package crain.client.game.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * This enum helps us map the ItemInfo -> How to write that into the Game.
 * This lets us keep a cleaner Data structure, minus the amount of Enums.
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public enum ItemCategory {

    INVENTORY_ITEM(new ItemInfo[]{
            ItemInfo.TELESCOPE, ItemInfo.BOATS_SAIL,
            ItemInfo.WIND_WAKER, ItemInfo.GRAPPLING_HOOK,
            ItemInfo.SPOILS_BAG, ItemInfo.TINGLE_TUNER,
            ItemInfo.IRON_BOOTS, ItemInfo.MAGIC_ARMOR,
            ItemInfo.BAIT_BAG, ItemInfo.BOOMERANG,
            ItemInfo.DEKU_LEAF, ItemInfo.BOMBS,
            ItemInfo.DELIVERY_BAG, ItemInfo.HOOKSHOT,
            ItemInfo.SKULL_HAMMER
    }),
    RUPEES(new ItemInfo[]{
            ItemInfo.GREEN_RUPEE, ItemInfo.BLUE_RUPEE,
            ItemInfo.YELLOW_RUPEE, ItemInfo.RED_RUPEE,
            ItemInfo.PURPLE_RUPEE, ItemInfo.ORANGE_RUPEE,
            ItemInfo.SILVER_RUPEE, ItemInfo.TINGLE_RUPEE
    }),
    PROGRESSIVE_ITEMS(new ItemInfo[]{
            ItemInfo.HEROS_SWORD, ItemInfo.MASTER_SWORD_ONE,
            ItemInfo.MASTER_SWORD_HALF, ItemInfo.MASTER_SWORD_FULL,
            ItemInfo.HEROS_SHIELD, ItemInfo.MIRROR_SHIELD,
            ItemInfo.PICTO_BOX, ItemInfo.DELUXE_PICTO_BOX,
            ItemInfo.HEROS_BOW, ItemInfo.FIRE_AND_ICE_ARROWS,
            ItemInfo.LIGHT_ARROWS
    }),
    SHARDS(new ItemInfo[]{
            ItemInfo.TRIFORCE_SHARD_ONE, ItemInfo.TRIFORCE_SHARD_TWO,
            ItemInfo.TRIFORCE_SHARD_THREE, ItemInfo.TRIFORCE_SHARD_FOUR,
            ItemInfo.TRIFORCE_SHARD_FIVE, ItemInfo.TRIFORCE_SHARD_SIX,
            ItemInfo.TRIFORCE_SHARD_SEVEN, ItemInfo.TRIFORCE_SHARD_EIGHT
    }),
    STATUES(new ItemInfo[]{
            ItemInfo.DRAGON_TINGLE_STATUE, ItemInfo.FORBIDDEN_TINGLE_STATUE,
            ItemInfo.GODDESS_TINGLE_STATUE, ItemInfo.EARTH_TINGLE_STATUE,
            ItemInfo.WIND_TINGLE_STATUE
    }),
    SONGS(new ItemInfo[]{
            ItemInfo.WINDS_REQUIEM, ItemInfo.BALLAD_OF_GALES,
            ItemInfo.COMMAND_MELODY, ItemInfo.EARTH_GODS_LYRIC,
            ItemInfo.WIND_GODS_ARIA, ItemInfo.SONG_OF_PASSING
    }),
    WALLET(new ItemInfo[]{
            ItemInfo.THOUSAND_RUPEE_WALLET, ItemInfo.FIVE_THOUSAND_RUPEE_WALLET
    }),
    PROGRESSIVE_CONSUMABLE(new ItemInfo[]{
            ItemInfo.SIXTY_BOMB_BAG, ItemInfo.MAX_BOMB_BAG,
            ItemInfo.SIXTY_QUIVER, ItemInfo.MAX_QUIVER
    }),
    PEARLS(new ItemInfo[]{
            ItemInfo.DINS_PEARL, ItemInfo.FARORES_PEARL,
            ItemInfo.NAYRUS_PEARL
    }),
    DELIVERY_BAG_ITEMS(new ItemInfo[]{
            ItemInfo.NOTE_TO_MOM, ItemInfo.MAGGIES_LETTER,
            ItemInfo.MOBLINS_LETTER, ItemInfo.CABANA_DEED,
            ItemInfo.FILL_UP_COUPON, ItemInfo.COMPLIMENTARY_ID,
            ItemInfo.FATHERS_LETTER
    }),
    DUNGEON_ITEMS(new ItemInfo[]{
            ItemInfo.DRC_BIG_KEY, ItemInfo.DRC_DUNGEON_COMPASS,
            ItemInfo.DRC_DUNGEON_MAP, ItemInfo.FW_BIG_KEY,
            ItemInfo.FW_DUNGEON_COMPASS, ItemInfo.FW_DUNGEON_MAP,
            ItemInfo.TOTG_BIG_KEY, ItemInfo.TOTG_DUNGEON_COMPASS,
            ItemInfo.TOTG_DUNGEON_MAP, ItemInfo.FF_DUNGEON_COMPASS,
            ItemInfo.FF_DUNGEON_MAP, ItemInfo.ET_BIG_KEY,
            ItemInfo.ET_DUNGEON_COMPASS, ItemInfo.ET_DUNGEON_MAP,
            ItemInfo.WT_BIG_KEY, ItemInfo.WT_DUNGEON_COMPASS,
            ItemInfo.WT_DUNGEON_MAP
    }),
    SMALL_KEYS(new ItemInfo[]{
            ItemInfo.DRC_SMALL_KEY, ItemInfo.FW_SMALL_KEY,
            ItemInfo.TOTG_SMALL_KEY, ItemInfo.ET_SMALL_KEY,
            ItemInfo.WT_SMALL_KEY
    }),
    CHARTS(new ItemInfo[]{
            ItemInfo.TREASURE_CHART_ONE, ItemInfo.TREASURE_CHART_TWO, ItemInfo.TREASURE_CHART_THREE,
            ItemInfo.TREASURE_CHART_FOUR, ItemInfo.TREASURE_CHART_FIVE, ItemInfo.TREASURE_CHART_SIX,
            ItemInfo.TREASURE_CHART_SEVEN, ItemInfo.TREASURE_CHART_EIGHT, ItemInfo.TREASURE_CHART_NINE,
            ItemInfo.TREASURE_CHART_TEN, ItemInfo.TREASURE_CHART_ELEVEN, ItemInfo.TREASURE_CHART_TWELVE,
            ItemInfo.TREASURE_CHART_THIRTEEN, ItemInfo.TREASURE_CHART_FOURTEEN, ItemInfo.TREASURE_CHART_FIFTEEN,
            ItemInfo.TREASURE_CHART_SIXTEEN, ItemInfo.TREASURE_CHART_SEVENTEEN, ItemInfo.TREASURE_CHART_EIGHTEEN,
            ItemInfo.TREASURE_CHART_NINETEEN, ItemInfo.TREASURE_CHART_TWENTY, ItemInfo.TREASURE_CHART_TWENTY_ONE,
            ItemInfo.TREASURE_CHART_TWENTY_TWO, ItemInfo.TREASURE_CHART_TWENTY_THREE, ItemInfo.TREASURE_CHART_TWENTY_FOUR,
            ItemInfo.TREASURE_CHART_TWENTY_FIVE, ItemInfo.TREASURE_CHART_TWENTY_SIX, ItemInfo.TREASURE_CHART_TWENTY_SEVEN,
            ItemInfo.TREASURE_CHART_TWENTY_EIGHT, ItemInfo.TREASURE_CHART_TWENTY_NINE, ItemInfo.TREASURE_CHART_THIRTY,
            ItemInfo.TREASURE_CHART_THIRTY_ONE, ItemInfo.TREASURE_CHART_THIRTY_TWO, ItemInfo.TREASURE_CHART_THIRTY_THREE,
            ItemInfo.TREASURE_CHART_THIRTY_FOUR, ItemInfo.TREASURE_CHART_THIRTY_FIVE, ItemInfo.TREASURE_CHART_THIRTY_SIX,
            ItemInfo.TREASURE_CHART_THIRTY_SEVEN, ItemInfo.TREASURE_CHART_THIRTY_EIGHT, ItemInfo.TREASURE_CHART_THIRTY_NINE,
            ItemInfo.TREASURE_CHART_FORTY, ItemInfo.TREASURE_CHART_FORTY_ONE,
            // Triforce Charts
            ItemInfo.TRIFORCE_CHART_ONE, ItemInfo.TRIFORCE_CHART_TWO, ItemInfo.TRIFORCE_CHART_THREE,
            ItemInfo.TRIFORCE_CHART_FOUR, ItemInfo.TRIFORCE_CHART_FIVE, ItemInfo.TRIFORCE_CHART_SIX,
            ItemInfo.TRIFORCE_CHART_SEVEN, ItemInfo.TRIFORCE_CHART_EIGHT,
            //Non-Salvage Charts
            ItemInfo.SUBMARINE_CHART, ItemInfo.BEEDLES_CHART, ItemInfo.PLATFORM_CHART,
            ItemInfo.LIGHT_RING_CHART, ItemInfo.SECRET_CAVE_CHART, ItemInfo.SEA_HEARTS_CHART,
            ItemInfo.ISLAND_HEARTS_CHART, ItemInfo.GREAT_FAIRY_CHART, ItemInfo.OCTO_CHART,
            ItemInfo.INCREDIBLE_CHART, ItemInfo.GHOST_SHIP_CHART, ItemInfo.TINGLES_CHART
    }),
    BAIT_BAG_CONSUMABLES(new ItemInfo[]{
            ItemInfo.HYOI_PEAR, ItemInfo.ALL_PURPOSE_BAIT
    }),
    SPOILS_BAG_CONSUMABLE(new ItemInfo[]{
            ItemInfo.RED_CHU_JELLY, ItemInfo.GREEN_CHU_JELLY,
            ItemInfo.GOLDEN_FEATHER, ItemInfo.BOKO_BABA_SEED,
            ItemInfo.KNIGHTS_CREST, ItemInfo.SKULL_NECKLACE,
            ItemInfo.JOY_PENDANT
    }),
    BIT_ONLY_ACTION(new ItemInfo[]{
            ItemInfo.LEGENDARY_PICTOGRAPH, ItemInfo.FIRST_TINGLE_REWARD,
            ItemInfo.SECOND_TINGLE_REWARD, ItemInfo.THIRD_TINGLE_REWARD,
            ItemInfo.FOURTH_TINGLE_REWARD, ItemInfo.FIFTH_TINGLE_REWARD,
            ItemInfo.TOWN_FLOWER, ItemInfo.SEA_FLOWER,
            ItemInfo.EXOTIC_FLOWER, ItemInfo.HEROS_FLAG,
            ItemInfo.BIG_CATCH_FLAG, ItemInfo.BIG_SALE_FLAG,
            ItemInfo.PINWHEEL, ItemInfo.SICKLE_MOON_FLAG,
            ItemInfo.SKULL_TOWER_IDOL, ItemInfo.FOUNTAIN_IDOL,
            ItemInfo.POSTMAN_STATUE,
            ItemInfo.SHOP_GURU_STATUE, ItemInfo.BLUE_CHU_JELLY,
    }),
    TRANSLATED_CHARTS(new ItemInfo[]{
            ItemInfo.TRANSLATED_CHART_ONE, ItemInfo.TRANSLATED_CHART_TWO,
            ItemInfo.TRANSLATED_CHART_THREE, ItemInfo.TRANSLATED_CHART_FOUR,
            ItemInfo.TRANSLATED_CHART_FIVE, ItemInfo.TRANSLATED_CHART_SIX,
            ItemInfo.TRANSLATED_CHART_SEVEN, ItemInfo.TRANSLATED_CHART_EIGHT
    }),
    BOTTLES(new ItemInfo[]{
            ItemInfo.EMPTY_BOTTLE,
            // Just to put all the Bottle Logic in one place,
            // Only the Empty bottle is support right now.
            ItemInfo.RED_POTION, ItemInfo.GREEN_POTION,
            ItemInfo.BLUE_POTION, ItemInfo.ELIXIR_SOUP,
            ItemInfo.ELIXIR_SOUP_HALF, ItemInfo.FOREST_WATER,
            ItemInfo.FOREST_FIREFLY, ItemInfo.BOTTLED_WATER,
            ItemInfo.FAIRY_IN_BOTTLE
    }),
    MISC(new ItemInfo[]{
            ItemInfo.PIECE_OF_HEART, ItemInfo.PIECE_OF_HEART_ALT,
            ItemInfo.HEART_CONTAINER, ItemInfo.HURRICANE_SPIN,
            ItemInfo.MAGIC_METER_UPGRADE, ItemInfo.HEROS_CHARM,
            ItemInfo.POWER_BRACELETS,
    }),
    NOT_SUPPORTED(new ItemInfo[]{
            ItemInfo.INVALID_ID, ItemInfo.PIRATES_CHARM,
            ItemInfo.HEROS_CLOTHES, ItemInfo.HEROS_NEW_CLOTHES,
            ItemInfo.SMALL_MAGIC_JAR, ItemInfo.LARGE_MAGIC_JAR,
            ItemInfo.SMALL_KEY, ItemInfo.GENERIC_COMPASS,
            ItemInfo.GENERIC_BIG_KEY, ItemInfo.GENERIC_DUNGEON_MAP,
            ItemInfo.ZELDA_HEARTS, ItemInfo.TEN_ARROWS,
            ItemInfo.TWENTY_ARROWS, ItemInfo.THIRTY_ARROWS,
            ItemInfo.FIVE_BOMBS, ItemInfo.TEN_BOMBS,
            ItemInfo.TWENTY_BOMBS, ItemInfo.THIRTY_BOMBS,
            ItemInfo.FAIRY, ItemInfo.HEART,
            ItemInfo.YELLOW_RUPEE_JOKE, ItemInfo.RECOVERED_HEROS_SWORD
    });

    private final ItemInfo[] items;

    public static ItemCategory getInfoCategory(ItemInfo info) {
        log.trace("Checking Category for {}", info.getDisplayName());
        for (ItemCategory category : ItemCategory.values()) {
            Optional<ItemInfo> filteredInfo = Arrays.stream(category.getItems())
                    .filter(itemInfo -> Objects.equals(itemInfo, info))
                    .findFirst();
            if (filteredInfo.isPresent()) {
                return category;
            }
        }
        log.trace("No Item Category found");
        return ItemCategory.NOT_SUPPORTED;
    }

    public static ItemCategory getItemCategory(Integer itemId) {
        return ItemCategory.getInfoCategory(ItemInfo.getInfoByItemId(itemId));
    }
}
