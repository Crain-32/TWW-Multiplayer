package crain.client.game.scanners;

import constants.WorldType;
import crain.client.events.ItemFoundEvent;
import crain.client.game.GameInfoConfig;
import crain.client.game.data.ItemCategory;
import crain.client.game.data.ItemInfo;
import crain.client.game.data.MemoryConstants;
import crain.client.game.interfaces.MemoryAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class CheckScanner implements Runnable {
    private final MemoryAdapter memoryAdapter;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final GameInfoConfig gameConfig;

    @Override
    public void run() {
        log.debug("Now Scanning for Info at Item Address: {} And World Address: {}", Integer.toHexString(gameConfig.getItemIdAddress()), Integer.toHexString(gameConfig.getWorldIdAddress()));
        if (Objects.isNull(memoryAdapter) || !memoryAdapter.isConnected()) {
            log.debug("No Handler Found, or Handler was not Connected");
            return;
        }
        String currStage = memoryAdapter.readString(MemoryConstants.currStageName, 8);
        if (StringUtils.equalsIgnoreCase(currStage, "Name") || StringUtils.equalsIgnoreCase(currStage, "sea_T")) {
            log.trace("Current Stage {}", currStage);
            memoryAdapter.writeInteger(gameConfig.getItemIdAddress(), 0);
            memoryAdapter.writeInteger(gameConfig.getWorldIdAddress(), 0);
        }
        int itemId = memoryAdapter.readInteger(gameConfig.getItemIdAddress());
        int worldId = memoryAdapter.readInteger(gameConfig.getWorldIdAddress());

        if (itemId == 0 || (worldId == 0 && (gameConfig.getWorldType() != WorldType.COOP))) {
            log.trace("World ID: {}", worldId);
            log.trace("Item ID: {}", itemId);
            return;
        }
        log.debug("Clearing Game Memory");
        memoryAdapter.writeByte(gameConfig.getItemIdAddress(), (byte) 0);
        memoryAdapter.writeByte(gameConfig.getWorldIdAddress(), (byte) 0);
        if (gameConfig.getWorldType() == WorldType.COOP) {
            if (shouldSend(itemId)) {
                log.debug("Publishing Coop Record");
                applicationEventPublisher.publishEvent(new ItemFoundEvent(ItemInfo.getInfoByItemId(itemId), null, null));
            } else {
                log.debug("Not Sending Item: {}", ItemInfo.getInfoByItemId(itemId).getDisplayName());
            }
        } else if (gameConfig.getWorldType() == WorldType.MULTIWORLD || gameConfig.getWorldType() == WorldType.SHARED) {
            log.debug("Publishing Multiworld Record");
            applicationEventPublisher.publishEvent(new ItemFoundEvent(ItemInfo.getInfoByItemId(itemId), worldId, null));
        } else {
            log.debug("Failed to identify the World Type: {}", gameConfig.getWorldType());
        }
    }

    private Boolean shouldSend(Integer itemId) {
        ItemCategory category = ItemCategory.getItemCategory(itemId);
        return switch (category) {
            case BIT_ONLY_ACTION, RUPEES, NOT_SUPPORTED, TRANSLATED_CHARTS, SPOILS_BAG_CONSUMABLE, BAIT_BAG_CONSUMABLES -> false;
            default -> true;
        };
    }

//    private ItemFoundEvent checkForItem() {
//
//    }
}
