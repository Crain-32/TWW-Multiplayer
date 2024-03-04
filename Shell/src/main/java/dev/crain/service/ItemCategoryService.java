package dev.crain.service;

import dev.crain.exceptions.FailedToTakeItemException;
import dev.crain.game.data.ItemCategory;
import dev.crain.game.data.ItemInfo;
import dev.crain.game.interfaces.ItemCategoryHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@Primary
public class ItemCategoryService {

    private final Map<ItemCategory, ItemCategoryHandler> resolvedHandlers;

    private final HashMap<ScheduledFuture<?>, ItemInfo> queuedItems = new HashMap<>();
    private final ThreadPoolTaskScheduler scheduledExecutorService;

    public ItemCategoryService(List<? extends ItemCategoryHandler> handlers, ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.scheduledExecutorService = threadPoolTaskScheduler;
        resolvedHandlers = new HashMap<>();

        for (ItemCategory category : ItemCategory.values()) {
            log.debug("Registering {}", category);
            ItemCategoryHandler supported = handlers.stream()
                    .filter(
                            handler -> handler.supports(category)
                    ).findFirst().orElseThrow(IllegalStateException::new);
            log.debug("Registered: {} to {}", category, supported.getClass().getSimpleName());
            resolvedHandlers.put(category, supported);
        }

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            log.debug("Checking Queued Items");
            if (!queuedItems.isEmpty()) {
                log.debug("Checking Scheduled Futures, Amount: {}", queuedItems.size());
                for (ScheduledFuture<?> scheduledFuture : queuedItems.keySet()) {
                    if (scheduledFuture.isDone()) {
                        try {
                            scheduledFuture.get();
                            queuedItems.remove(scheduledFuture);
                        } catch (Exception e) {
                            try {
                                log.debug("Failed to give Item with the following Exception", e);
                                ItemInfo info = queuedItems.get(scheduledFuture);
                                ItemCategoryHandler handler = resolvedHandlers.get(ItemCategory.getInfoCategory(info));
                                ScheduledFuture<?> redo = scheduledExecutorService.schedule(new ItemGiver(handler, info), Instant.now().plusSeconds(5));
                                queuedItems.remove(scheduledFuture);
                                queuedItems.put(redo, info);
                            } catch (Exception ex) {
                                log.debug("Failed to Rescheduling the Item with the following Exception", ex);
                            }
                        }
                    } else if (scheduledFuture.isCancelled()) {
                        log.debug("Item Giving Task was Cancelled");
                        queuedItems.remove(scheduledFuture);
                    }
                }
                log.debug("Remaining Futures: {}", queuedItems.size());
            }
        }, Duration.ofMillis(30000));
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
            ScheduledFuture<?> scheduledFuture = scheduledExecutorService.schedule(giver, Instant.now().plusSeconds(30));
            queuedItems.put(scheduledFuture, info);
        }
    }

    public Boolean takeItem(ItemInfo info) throws FailedToTakeItemException {
        ItemCategory category = ItemCategory.getInfoCategory(info);
        ItemCategoryHandler categoryHandler = resolvedHandlers.get(category);
        return categoryHandler.giveItem(info);
    }

    private record ItemGiver(ItemCategoryHandler handler, ItemInfo itemToGive) implements Runnable {
        @Override
        public void run() {
            log.debug("Attempting Delayed Give Item");
            handler.giveItem(itemToGive);
        }
    }
}
