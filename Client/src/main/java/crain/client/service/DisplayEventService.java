package crain.client.service;

import constants.WorldType;
import crain.client.config.GameRoomConfig;
import crain.client.game.data.ItemInfo;
import crain.client.view.events.GeneralMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import records.INFO;
import records.ROOM;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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

    private final Map<Integer, String> worldIdToUserName = new HashMap<>();


    @Async
    @EventListener(condition = "!T(crain.client.util.NullUtil).anyNullField(itemRecord)")
    public void foundItem(INFO.ItemRecord itemRecord) {
        String sourcePlayerName = getUsername(itemRecord.sourcePlayerWorldId());
        String targetPlayerName = getUsername(itemRecord.targetPlayerWorldId());
        String itemDisplayName = ItemInfo.getInfoByItemId(itemRecord.itemId()).getDisplayName();
        applicationEventPublisher.publishEvent(new GeneralMessageEvent(itemSentOut.formatted(sourcePlayerName, targetPlayerName, itemDisplayName)));
    }

    @Async
    @EventListener(condition = "!T(crain.client.util.NullUtil).anyNull(itemRecord?.itemId())")
    public void foundItem(INFO.CoopItemRecord itemRecord) {
        String sourcePlayer = Optional.of(itemRecord.sourcePlayer()).orElseGet(gameRoomConfig::getPlayerName);
        String itemDisplayName = ItemInfo.getInfoByItemId(itemRecord.itemId()).getDisplayName();
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

}
