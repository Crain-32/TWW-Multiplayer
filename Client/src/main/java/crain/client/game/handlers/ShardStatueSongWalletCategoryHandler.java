package crain.client.game.handlers;

import crain.client.exceptions.FailedToGiveItemException;
import crain.client.exceptions.FailedToTakeItemException;
import crain.client.game.data.ItemCategory;
import crain.client.game.data.ItemInfo;
import crain.client.game.interfaces.ItemCategoryHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class ShardStatueSongWalletCategoryHandler extends ItemCategoryHandler {

    private final List<ItemCategory> supportedCategories = Arrays.asList(
            ItemCategory.SHARDS,
            ItemCategory.SONGS,
            ItemCategory.SHARDS,
            ItemCategory.STATUES,
            ItemCategory.WALLET
    );

    @Override
    public Boolean supports(ItemCategory itemCategory) {
        return supportedCategories.stream().anyMatch(category -> category == itemCategory);
    }

    @Override
    public Boolean giveItem(ItemInfo info) throws FailedToGiveItemException {
        try {
            Integer consoleAddress = getMemoryAddress(ItemCategory.getInfoCategory(info));
            Integer flagIndex = getFlagIndex(info);
            Byte currFlagValue = memoryAdapter.readByte(consoleAddress);
            Boolean successfulWrite = memoryAdapter.writeByte(consoleAddress, (byte) (currFlagValue | (byte) 1 << flagIndex));
            if (!successfulWrite) {
                throw new FailedToGiveItemException("Failed to Write to Item Flag with current Value " + currFlagValue);
            }
            return true;
        } catch (FailedToGiveItemException e) {
            throw e;
        } catch (Exception e) {
            throw new FailedToGiveItemException(e.getMessage(), info, e);
        }
    }

    @Override
    public Boolean takeItem(ItemInfo info) throws FailedToTakeItemException {
        try {
            Integer consoleAddress = getMemoryAddress(ItemCategory.getInfoCategory(info));
            Integer flagIndex = getFlagIndex(info);
            Byte currFlagValue = memoryAdapter.readByte(consoleAddress);
            Byte mask = (byte) (0b11111111 ^ (1 << flagIndex));
            Boolean successfulWrite = memoryAdapter.writeByte(consoleAddress, (byte) (currFlagValue & mask));
            if (!successfulWrite) {
                throw new FailedToTakeItemException("Failed to Write to Item Flag with current Value " + currFlagValue);
            }
            return true;
        } catch (FailedToTakeItemException e) {
            throw e;
        } catch (Exception e) {
            throw new FailedToTakeItemException(e.getMessage(), info, e);
        }
    }

    @Override
    protected String getClassName() {
        return this.getClass().getSimpleName();
    }

    private Integer getMemoryAddress(ItemCategory category) {
        return switch (category) {
            case SHARDS -> 0x803C4CC6;
            case SONGS -> 0x803C4CC5;
            case STATUES -> 0x803C5296;
            case WALLET -> 0x803C4C1A;
            default -> throw new IllegalArgumentException("The Item Category Provided is not Supported: " + category);
        };
    }

    private Integer getFlagIndex(ItemInfo info) {
        return switch (info) {
            case TRIFORCE_SHARD_ONE, WINDS_REQUIEM, THOUSAND_RUPEE_WALLET -> 0;
            case TRIFORCE_SHARD_TWO, BALLAD_OF_GALES, FIVE_THOUSAND_RUPEE_WALLET -> 1;
            case TRIFORCE_SHARD_THREE, COMMAND_MELODY, DRAGON_TINGLE_STATUE -> 2;
            case TRIFORCE_SHARD_FOUR, EARTH_GODS_LYRIC, FORBIDDEN_TINGLE_STATUE -> 3;
            case TRIFORCE_SHARD_FIVE, WIND_GODS_ARIA, GODDESS_TINGLE_STATUE -> 4;
            case TRIFORCE_SHARD_SIX, SONG_OF_PASSING, EARTH_TINGLE_STATUE -> 5;
            case TRIFORCE_SHARD_SEVEN, WIND_TINGLE_STATUE -> 6;
            case TRIFORCE_SHARD_EIGHT -> 7;
            default -> throw new IllegalArgumentException("The Item Info Provided is not Supported: " + info);
        };
    }
}
