package client.game;

import client.exceptions.FailedToTakeItemException;
import client.game.data.ItemCategory;
import client.game.data.ItemInfo;
import client.game.interfaces.ItemCategoryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
public class ItemCategoryService {

    private final Map<ItemCategory, ItemCategoryHandler> resolvedHandlers;
    private HashMap<ScheduledFuture<ItemGiver>, ItemInfo> queuedItems = new HashMap<>();
    private final ThreadPoolTaskScheduler scheduledExecutorService;

    public ItemCategoryService(List<? extends ItemCategoryHandler> handlers, ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.scheduledExecutorService = threadPoolTaskScheduler;
        resolvedHandlers = new HashMap<>();

        for (ItemCategory category : ItemCategory.values()) {
            log.debug("Registering " + category);
            ItemCategoryHandler supported = handlers.stream()
                    .filter(
                            handler -> handler.supports(category)
                    ).findFirst().orElseThrow(IllegalStateException::new);
            log.debug("Registered: " + category + " to " + supported.getClass().getSimpleName());
            resolvedHandlers.put(category, supported);
        }

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            log.debug("Checking Queued Items");
            if (queuedItems.size() != 0) {
                log.debug("Checking Scheduled Futures, Amount: " + queuedItems.size());
                for (ScheduledFuture<ItemGiver> scheduledFuture : queuedItems.keySet()) {
                    if (scheduledFuture.isDone()) {
                        try {
                            scheduledFuture.get();
                            queuedItems.remove(scheduledFuture);
                        } catch (Exception e) {
                            try {
                                log.debug("Failed to give Item with the following Exception", e);
                                ItemInfo info = queuedItems.get(scheduledFuture);
                                ItemCategoryHandler handler = resolvedHandlers.get(ItemCategory.getInfoCategory(info));
                                ScheduledFuture<ItemGiver> redo = (ScheduledFuture<ItemGiver>) scheduledExecutorService.schedule(new ItemGiver(handler, info), Instant.now().plusSeconds(5));
                                queuedItems.remove(scheduledFuture);
                                queuedItems.put(redo, info);
                            } catch (Exception ex) {
                                log.debug("What?", ex);
                            }
                        }
                    } else if (scheduledFuture.isCancelled()) {
                        log.debug("Item Giving Task was Cancelled");
                        queuedItems.remove(scheduledFuture);
                    }
                }
                log.debug("Remaining Futures: " + queuedItems.size());
            }
        }, 30000);
    }


    public void giveItem(Integer itemId) {
        giveItem(ItemInfo.getInfoByItemId(itemId));
    }

    public void giveItem(ItemInfo info) {
        ItemCategory category = ItemCategory.getInfoCategory(info);
        ItemCategoryHandler categoryHandler = resolvedHandlers.get(category);
        try {
            category = ItemCategory.getInfoCategory(info);
            categoryHandler = resolvedHandlers.get(category);
            if (!categoryHandler.canGiveItem()) {
                throw new IllegalStateException("The Handler can't give the item");
            }
            categoryHandler.giveItem(info);
        } catch (Exception e) {
            log.debug("Failed to give Item", e);
            ItemGiver giver = new ItemGiver(categoryHandler, info);
            ScheduledFuture<ItemGiver> scheduledFuture = (ScheduledFuture<ItemGiver>) scheduledExecutorService.schedule(giver, Instant.now().plusSeconds(30));
            queuedItems.put(scheduledFuture, info);
        }
    }

    public Boolean takeItem(ItemInfo info) throws FailedToTakeItemException {
        ItemCategory category = ItemCategory.getInfoCategory(info);
        ItemCategoryHandler categoryHandler = resolvedHandlers.get(category);
        return categoryHandler.giveItem(info);
    }

    @RequiredArgsConstructor
    private class ItemGiver implements Runnable {
        private final ItemCategoryHandler handler;
        private final ItemInfo itemToGive;

        @Override
        public void run() {
            log.debug("Attempting Delayed Give Item");
            handler.giveItem(itemToGive);
        }
    }
}
