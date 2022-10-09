package client.game.interfaces;

import client.exceptions.FailedToGiveItemException;
import client.exceptions.FailedToTakeItemException;
import client.exceptions.MissingMemoryHandlerException;
import client.game.GameInterfaceEvents;
import client.game.data.ItemCategory;
import client.game.data.ItemInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;


/**
 * supports(ItemCategory) returns true on item category.
 */
@Slf4j
public abstract class ItemCategoryHandler {

    protected MemoryAdapter memoryAdapter = null;

    @Async
    @EventListener
    public void setMemoryAdapterEvent(GameInterfaceEvents.MemoryHandlerEvent event) {
        log.debug("Memory Adapter Updated: {}", event.memoryAdapter().getClass().getSimpleName());
        this.memoryAdapter = event.memoryAdapter();
    }

    protected void verifyHandler() throws MissingMemoryHandlerException, IllegalStateException {
        if (memoryAdapter == null) {
            throw new MissingMemoryHandlerException("No Memory Adapter could be found");
        }
        if (log.isTraceEnabled()) {
            log.trace("Current Memory Adapter: {}", memoryAdapter.getClass().getSimpleName());
        }
        if (!memoryAdapter.isConnected()) {
            throw new IllegalStateException("Memory Adapter is not connected");
        }
    }

    /**
     * If the state of the game doesn't prevent giving the item
     */
    public Boolean canGiveItem() {
        verifyHandler();
        String currStage = memoryAdapter.readString(0x803C9D3C, 8);
        log.trace("Current Stage {}", currStage);
        return !StringUtils.equalsIgnoreCase(currStage, "Name") && !StringUtils.equalsIgnoreCase(currStage, "sea_T");
    }

    public abstract Boolean supports(ItemCategory itemCategory);

    public abstract Boolean giveItem(ItemInfo info) throws FailedToGiveItemException;

    public abstract Boolean takeItem(ItemInfo info) throws FailedToTakeItemException;
}
