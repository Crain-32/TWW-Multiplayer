package crain.client.game.handlers;

import crain.client.exceptions.FailedToGiveItemException;
import crain.client.exceptions.FailedToTakeItemException;
import crain.client.game.data.*;
import crain.client.game.interfaces.ItemCategoryHandler;
import crain.client.game.util.MemoryEditorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class DungeonItemHandler extends ItemCategoryHandler {

    private final List<ItemCategory> supportedCategories = Arrays.asList(ItemCategory.DUNGEON_ITEMS, ItemCategory.SMALL_KEYS);

    private static final Integer smallKeyOffset = 0x20;
    private static final Integer dungeonFlagOffset = 0x21;
    private static final Integer mapOffset = 0;
    private static final Integer compassOffset = 1;
    private static final Integer bigKeyOffset = 2;

    @Override
    public Boolean supports(ItemCategory itemCategory) {
        return supportedCategories.stream().anyMatch(cat -> cat == itemCategory);
    }

    @Override
    public Boolean giveItem(ItemInfo info) throws FailedToGiveItemException {
        try {
            if (ItemCategory.getInfoCategory(info) == ItemCategory.SMALL_KEYS) {
                return giveSmallKey(info);
            }
            Byte stageId = DungeonStageInfo.stageIdFromItem(info);
            Integer flagOffset = getFlagOffset(info);
            Boolean gaveItem = toggleDungeonFlag(stageId, flagOffset, true);
            if (!gaveItem) {
                throw new FailedToGiveItemException("Failed to enable Dungeon Flag!", info);
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
            if (ItemCategory.getInfoCategory(info) == ItemCategory.SMALL_KEYS) {
                return takeSmallKey(info);
            }
            Byte stageId = DungeonStageInfo.stageIdFromItem(info);
            Integer flagOffset = getFlagOffset(info);
            Boolean gaveItem = toggleDungeonFlag(stageId, flagOffset, false);
            if (!gaveItem) {
                throw new FailedToTakeItemException("Failed to disable Dungeon Flag!", info);
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

    /**
     * Small Keys need special Logic, so we route them through here.
     */
    private Boolean giveSmallKey(ItemInfo info) throws FailedToGiveItemException {
        Byte stageId = DungeonStageInfo.stageIdFromItem(info);
        Byte currStageId = memoryAdapter.readByte(MemoryConstants.currStageAddress);
        boolean gaveItem;
        if (!Objects.equals(currStageId, stageId)) {
            Integer stageAddress = StageFlagInfo.fromStageId(stageId).getMemoryLocation();
            Byte currKeyAmount = memoryAdapter.readByte(stageAddress + smallKeyOffset);
            gaveItem = memoryAdapter.writeByte(stageAddress + smallKeyOffset, (byte) (currKeyAmount + 1));
        } else {
            gaveItem = memoryAdapter.writeShort(MemoryConstants.incrementSmallKey, (short) 1);
        }
        if (!gaveItem) {
            throw new FailedToGiveItemException("Failed to give Small Key to Stage: " + stageId, info);
        }
        return true;
    }

    private Boolean takeSmallKey(ItemInfo info) throws FailedToTakeItemException {
        Byte stageId = DungeonStageInfo.stageIdFromItem(info);
        Byte currStageId = memoryAdapter.readByte(MemoryConstants.currStageAddress);
        boolean tookItem;
        if (!Objects.equals(currStageId, stageId)) {
            Integer stageAddress = StageFlagInfo.fromStageId(stageId).getMemoryLocation();
            Byte currKeyAmount = memoryAdapter.readByte(stageAddress + smallKeyOffset);
            if (currKeyAmount > 0) {
                tookItem = memoryAdapter.writeByte(stageAddress + smallKeyOffset, (byte) (currKeyAmount - 1));
            } else {
                tookItem = true;
            }
        } else {
            tookItem = memoryAdapter.writeShort(MemoryConstants.incrementSmallKey, (short) 0xFFFF);
        }
        if (!tookItem) {
            throw new FailedToTakeItemException("Failed to take Small Key to Stage: " + stageId, info);
        }
        return true;
    }

    private Boolean toggleDungeonFlag(Byte stageId, Integer offset, Boolean enable) {
        int targetStageAddress;
        Byte currStageId = memoryAdapter.readByte(MemoryConstants.currStageAddress);
        if (Objects.equals(currStageId, stageId)) {
            targetStageAddress = StageFlagInfo.fromStageId(stageId).getMemoryLocation();
        } else {
            targetStageAddress = StageFlagInfo.CURRENT.getMemoryLocation();
        }
        return MemoryEditorUtil.toggleBit(memoryAdapter, targetStageAddress, offset, enable);
    }

    private Integer getFlagOffset(ItemInfo info) {
        return switch (info) {
            case DRC_BIG_KEY, FW_BIG_KEY, TOTG_BIG_KEY, ET_BIG_KEY, WT_BIG_KEY -> bigKeyOffset;
            case DRC_DUNGEON_MAP, FW_DUNGEON_MAP, TOTG_DUNGEON_MAP, ET_DUNGEON_MAP, WT_DUNGEON_MAP -> mapOffset;
            case DRC_DUNGEON_COMPASS, FW_DUNGEON_COMPASS, TOTG_DUNGEON_COMPASS, ET_DUNGEON_COMPASS, WT_DUNGEON_COMPASS ->
                    compassOffset;
            default -> throw new IllegalArgumentException("The Provided Item is not Supported: " + info);
        };
    }
}
