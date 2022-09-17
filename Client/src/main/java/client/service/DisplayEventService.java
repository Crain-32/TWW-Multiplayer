package client.service;

import client.config.GameRoomConfig;
import client.game.data.ItemInfo;
import client.view.events.GeneralMessageEvent;
import constants.WorldType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import records.INFO;
import records.ROOM;

import java.util.*;

/**
 * Service to handle different event's responses to try and centralize it a bit more.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DisplayEventService {

    private static final String itemSentOut = "%s found %s's %s";
    private static final String itemReceived = "%s found %s";

    private final ApplicationEventPublisher applicationEventPublisher;
    private final GameRoomConfig gameRoomConfig;

    private Map<Integer, String> worldIdToUserName = new HashMap<>();


    @Async
    @EventListener
    public void foundItem(INFO.ItemRecord itemRecord) {
        Integer sourcePlayer = itemRecord.sourcePlayerWorldId();
        Integer targetPlayer = itemRecord.targetPlayerWorldId();
        Integer itemId = itemRecord.itemId();
        if (anyNull(sourcePlayer, targetPlayer, itemId)) {
            log.debug("Unexpected NULL in itemRecord: " + itemRecord);
            return;
        }
        String sourcePlayerName = getUsername(sourcePlayer);
        String targetPlayerName = getUsername(targetPlayer);
        String itemDisplayName = ItemInfo.getInfoByItemId(itemId).getDisplayName();
        applicationEventPublisher.publishEvent(new GeneralMessageEvent(itemSentOut.formatted(sourcePlayerName, targetPlayerName, itemDisplayName)));
    }

    @Async
    @EventListener
    public void foundItem(INFO.CoopItemRecord itemRecord) {
        String sourcePlayer = Optional.ofNullable(itemRecord.sourcePlayer()).orElseGet(gameRoomConfig::getPlayerName);
        Integer itemId = itemRecord.itemId();
        if (itemId == null) {
            log.debug("Unexpected NULL in itemRecord: " + itemRecord);
            return;
        }
        String itemDisplayName = ItemInfo.getInfoByItemId(itemId).getDisplayName();
        applicationEventPublisher.publishEvent(new GeneralMessageEvent(itemReceived.formatted(sourcePlayer, itemDisplayName)));
    }

    @Async
    @EventListener
    public void newPlayerAdded(ROOM.PlayerRecord playerRecord) {
        if (gameRoomConfig.getWorldType() == WorldType.COOP) {
            if (StringUtils.isNotBlank(playerRecord.playerName()) && !StringUtils.equalsIgnoreCase(gameRoomConfig.getPlayerName(), playerRecord.playerName())) {
                applicationEventPublisher.publishEvent(new GeneralMessageEvent("%s has joined your Room.".formatted(playerRecord.playerName())));
            }
        }
        worldIdToUserName.put(playerRecord.worldId(), playerRecord.playerName());
        if (!Objects.equals(gameRoomConfig.getWorldId(), playerRecord.worldId())) {
            applicationEventPublisher.publishEvent(new GeneralMessageEvent("%s has joined your Room for World %s".formatted(playerRecord.playerName(), playerRecord.worldId())));
        }
    }

    private String getUsername(Integer worldId) {
        var name = Optional.ofNullable(worldIdToUserName.get(worldId));
        return name.orElseGet(() -> "World " + worldId);
    }

    private boolean anyNull(Object... objectArr) {
        return Arrays.stream(objectArr).anyMatch(Objects::isNull);
    }
}
