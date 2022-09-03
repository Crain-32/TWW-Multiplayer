package client.game.handlers;

import client.exceptions.FailedToGiveItemException;
import client.exceptions.FailedToTakeItemException;
import client.game.data.ItemCategory;
import client.game.data.ItemInfo;
import client.game.data.MemoryConstants;
import client.game.interfaces.ItemCategoryHandler;
import client.game.util.MemoryScanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BottleHandler extends ItemCategoryHandler {
    @Override
    public Boolean supports(ItemCategory itemCategory) {
        return itemCategory == ItemCategory.BOTTLES;
    }

    @Override
    public Boolean giveItem(ItemInfo info) throws FailedToGiveItemException {
        if (info != ItemInfo.EMPTY_BOTTLE) {
            return true; // We only care for Empty bottle right now
        }
        try {
            Integer freeIndex = MemoryScanUtil.findOpenIndex(memoryHandler, MemoryConstants.bottleArray, 4);
            if (freeIndex < 0) {
                log.debug("No free Space to give bottle!");
                return true;
            }
            Boolean writeSuccess = memoryHandler.writeByte(MemoryConstants.bottleArray + freeIndex, ItemInfo.EMPTY_BOTTLE.getItemId());
            if (!writeSuccess) {
                throw new FailedToGiveItemException("Failed to write the Bottle to Inventory", info);
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
        return true; // Bottles don't impact logic, so nothing to worry about.
    }
}
