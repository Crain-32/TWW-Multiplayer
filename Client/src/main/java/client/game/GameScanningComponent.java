package client.game;


import client.game.data.ItemCategory;
import client.game.interfaces.MemoryAdapter;
import client.view.events.GeneralMessageEvent;
import constants.WorldType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import records.INFO;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;


@Slf4j
@Component
@RequiredArgsConstructor
public class GameScanningComponent {

    private MemoryAdapter handler;
    private ScheduledFuture<?> itemScanner;

    private final GameInfoConfig gameInfoConfig;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ThreadPoolTaskScheduler scheduledExecutorService;


    @Async
    @EventListener
    public void changeHandler(GameInterfaceEvents.MemoryHandlerEvent event) {
        log.debug("Memory Handler changed to " + event.memoryAdapter().getClass().getSimpleName());
        handler = event.memoryAdapter();
        ItemScanner scanner = new ItemScanner(handler, applicationEventPublisher, gameInfoConfig);

        itemScanner = scheduledExecutorService.scheduleAtFixedRate(scanner, 5000);
        scheduledExecutorService.schedule(this::itemScannerStillRunning, Instant.now().plusSeconds(300));
    }

    private void itemScannerStillRunning() {
        try {
            if (itemScanner.isDone()) {
                log.debug("Unknown Exception closed the Memory Handler", (Throwable) itemScanner.get());
                applicationEventPublisher.publishEvent(new GeneralMessageEvent("Your Connection to the Game Closed. Please Disconnect and Retry"));
                itemScanner.cancel(true);
            } else {
                scheduledExecutorService.schedule(this::itemScannerStillRunning, Instant.now().plusSeconds(120));
            }
        } catch (Exception e) {
            log.debug("Failed to Reschedule Item Scanner Validation, Attempting to Cancel the Runnable");
            itemScanner.cancel(true);
        }
    }

    @RequiredArgsConstructor
    private class ItemScanner implements Runnable {
        private final MemoryAdapter memoryAdapter;
        private final ApplicationEventPublisher applicationEventPublisher;
        private final GameInfoConfig gameConfig;

        @Override
        public void run() {
            log.debug("Now Scanning for Info at Item Address: " + Integer.toHexString(gameConfig.getItemIdAddress()) + " And World Address: " + Integer.toHexString(gameConfig.getWorldIdAddress()) );
            if (Objects.isNull(memoryAdapter) || !memoryAdapter.isConnected()) {
                log.debug("No Handler Found, or Handler was not Connected");
                return;
            }
            String currStage = memoryAdapter.readString(0x803C9D3C, 8);
            if (StringUtils.equalsIgnoreCase(currStage, "Name") || StringUtils.equalsIgnoreCase(currStage, "sea_T")) {
                log.trace("Current Stage " + currStage);
                memoryAdapter.writeInteger(gameConfig.getItemIdAddress(), 0);
                memoryAdapter.writeInteger(gameConfig.getWorldIdAddress(), 0);
            }
            int itemId = (0xFF & memoryAdapter.readInteger(gameConfig.getItemIdAddress()));
            int worldId = (0xFF & memoryAdapter.readInteger(gameConfig.getWorldIdAddress()));

            if (itemId == 0 || (worldId == 0 && (gameInfoConfig.getWorldType() != WorldType.COOP))) {

                log.debug("Nothing Found");
                log.debug("World ID: " + worldId);
                log.debug("Item ID: " + itemId);
                return;
            }
            log.debug("Clearing Game Memory");
            memoryAdapter.writeInteger(gameConfig.getItemIdAddress(), 0);
            memoryAdapter.writeInteger(gameConfig.getWorldIdAddress(), 0);
            if (gameConfig.getWorldType() == WorldType.COOP && shouldSend(itemId)) {
                log.debug("Publishing Coop Record");
                applicationEventPublisher.publishEvent(new INFO.CoopItemRecord(null, itemId));
            } else if (gameInfoConfig.getWorldType() == WorldType.MULTIWORLD || gameInfoConfig.getWorldType() == WorldType.SHARED) {
                log.debug("Publishing Multiworld Record");
                applicationEventPublisher.publishEvent(new INFO.ItemRecord(gameConfig.getWorldId(), worldId, itemId));
            } else {
                log.debug("Failed to identify the World Type");
            }
        }

        private Boolean shouldSend(Integer itemId) {
            ItemCategory category = ItemCategory.getItemCategory(itemId);
            return switch (category) {
                case BIT_ONLY_ACTION, RUPEES, NOT_SUPPORTED, TRANSLATED_CHARTS, SPOILS_BAG_CONSUMABLE, BAIT_BAG_CONSUMABLES  -> false;
                default -> true;
            };
        }
    }
}
