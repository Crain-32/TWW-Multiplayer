package client.game.interfaces;

import client.exceptions.FailedToGiveItemException;
import client.exceptions.FailedToTakeItemException;
import client.exceptions.MissingMemoryHandlerException;
import client.game.GameInterfaceEvents;
import client.game.data.ItemCategory;
import client.game.data.ItemInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;


/**
 * supports(ItemCategory) returns true on item category.
 */
@Slf4j
public abstract class ItemCategoryHandler {

    protected MemoryHandler memoryHandler = null;

    @Async
    @EventListener
    public void memoryHandlerEvent(GameInterfaceEvents.MemoryHandlerEvent event) {
        log.debug("Memory Handler Updated :" + this.getClass().getSimpleName());
        this.memoryHandler = event.memoryHandler();
    }

    protected void verifyHandler() throws MissingMemoryHandlerException {
        if (memoryHandler == null) {
            throw new MissingMemoryHandlerException("No memory handler could be found");
        }
    }


    public abstract Boolean supports(ItemCategory itemCategory);

    public abstract Boolean giveItem(ItemInfo info) throws FailedToGiveItemException;

    public abstract Boolean takeItem(ItemInfo info) throws FailedToTakeItemException;
}
