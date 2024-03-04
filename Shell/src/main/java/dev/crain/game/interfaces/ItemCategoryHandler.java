package dev.crain.game.interfaces;

import dev.crain.exceptions.FailedToGiveItemException;
import dev.crain.exceptions.FailedToTakeItemException;
import dev.crain.exceptions.memory.MemoryHandlerException;
import dev.crain.game.data.ItemCategory;
import dev.crain.game.data.ItemInfo;
import dev.crain.service.MemoryAwareService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;


/**
 * supports(ItemCategory) returns true on item category.
 */
@Slf4j
public abstract class ItemCategoryHandler extends MemoryAwareService {

    /**
     * If the state of the game doesn't prevent giving the item
     */
    public Boolean canGiveItem() throws MemoryHandlerException {
        var memoryAdapter = getMemoryAdapter();
        String currStage = memoryAdapter.readString(0x803C9D3C, 8);
        log.trace("Current Stage {}", currStage);
        return !StringUtils.equalsIgnoreCase(currStage, "Name") && !StringUtils.equalsIgnoreCase(currStage, "sea_T");
    }

    public abstract Boolean supports(ItemCategory itemCategory);

    public abstract Boolean giveItem(ItemInfo info) throws FailedToGiveItemException;

    public abstract Boolean takeItem(ItemInfo info) throws FailedToTakeItemException;
}
