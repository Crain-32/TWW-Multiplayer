package client.game.handlers;

import client.exceptions.FailedToGiveItemException;
import client.exceptions.FailedToTakeItemException;
import client.game.data.ItemCategory;
import client.game.data.ItemInfo;
import client.game.interfaces.ItemCategoryHandler;
import client.game.util.ProgressiveItemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProgressiveCategoryHandler extends ItemCategoryHandler {


    @Override
    public Boolean supports(ItemCategory itemCategory) {
        return itemCategory == ItemCategory.PROGRESSIVE_ITEMS;
    }

    @Override
    public Boolean giveItem(ItemInfo info) throws FailedToGiveItemException {
        try {
            ItemInfo currentItem = getCurrentValue(info);
            ItemInfo nextProgressiveVal = getNextProgressiveItem(info, currentItem);
            if (nextProgressiveVal == ItemInfo.INVALID_ID) {
                return true;
            }
            Integer flagAddress = ProgressiveItemUtil.getProgressiveFlagAddress(nextProgressiveVal);
            Integer progressiveIndex = ProgressiveItemUtil.getProgressiveIndex(nextProgressiveVal);
            Byte currentFlagVal = memoryAdapter.readByte(flagAddress);
            if(!memoryAdapter.writeByte(flagAddress, (byte) (currentFlagVal | (byte) 1 << progressiveIndex))) {
                throw new FailedToGiveItemException("Failed to Updated Ownership Flag", info);
            }
            return memoryAdapter.writeByte(ProgressiveItemUtil.getProgressiveAddress(info), nextProgressiveVal.getItemId());
        } catch (FailedToGiveItemException e) {
            throw e;
        } catch (Exception exception) {
            throw new FailedToGiveItemException("Failed to give the Item", info, exception);
        }
    }

    @Override
    public Boolean takeItem(ItemInfo info) throws FailedToTakeItemException {
        return null;
    }


    private ItemInfo getCurrentValue(ItemInfo info) {
        Integer consoleAddress = ProgressiveItemUtil.getProgressiveAddress(info);
        Byte currentItemId = memoryAdapter.readByte(consoleAddress);
        return ItemInfo.getInfoByItemId(currentItemId);
    }

    /**
     * INVALID_ID means we need to progress in the next value, but we can't do that easily since the Switch case
     * won't allow for that. So we're doing it here instead.
     */
    private ItemInfo getNextProgressiveItem(ItemInfo targetItem, ItemInfo currentVal) {
        if (currentVal == ItemInfo.INVALID_ID) {
            return switch (targetItem) {
                case HEROS_SWORD, MASTER_SWORD_ONE, MASTER_SWORD_HALF, MASTER_SWORD_FULL -> ItemInfo.HEROS_SWORD;
                case HEROS_BOW, FIRE_AND_ICE_ARROWS, LIGHT_ARROWS -> ItemInfo.HEROS_BOW;
                case HEROS_SHIELD, MIRROR_SHIELD -> ItemInfo.HEROS_SHIELD;
                case PICTO_BOX, DELUXE_PICTO_BOX -> ItemInfo.PICTO_BOX;
                default -> throw new IllegalArgumentException("The Target Item is not a Progressive Item!");
            };
        } else if (ProgressiveItemUtil.getNextProgression(currentVal) != ItemInfo.INVALID_ID) {
            return ProgressiveItemUtil.getNextProgression(currentVal);
        } else {
            return ItemInfo.INVALID_ID;
        }
    }
}
