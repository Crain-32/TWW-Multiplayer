package client.communication.handlers;

import client.config.GameRoomConfig;
import client.game.ItemCategoryService;
import client.game.data.ItemInfo;
import client.view.events.GeneralMessageEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.WorldType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import records.INFO;

import java.lang.reflect.Type;
import java.util.Objects;

@Slf4j
@Component
@Qualifier("queueHandler")
@RequiredArgsConstructor
public class CoopQueueHandler extends AbstractQueueHandler {

    private final ObjectMapper objectMapper;
    //Improper, we need to route this through another Service First in order to accomplish the following,
    // Queue the Item if we can't get it, or fail to give it.
    // Display the Information to the User
    // Logging?
    // Manage the GameHandler.
    private final ItemCategoryService itemCategoryService;
    private final GameRoomConfig gameRoomConfig;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String getTopicPath() {
        return "/topic/coop/";
    }

    @Override
    public WorldType supportedWorldType() {
        return WorldType.COOP;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return INFO.CoopItemRecord.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, @Nullable Object payload) {
        try {
            log.debug("Received a Frame");
            INFO.CoopItemRecord itemRecord = objectMapper.convertValue(payload, INFO.CoopItemRecord.class);
            log.debug(itemRecord.toString());
            if (!Objects.equals(itemRecord.sourcePlayer(), gameRoomConfig.getPlayerName())) {
                ItemInfo item = ItemInfo.getInfoByItemId(itemRecord.itemId());
                applicationEventPublisher.publishEvent(new GeneralMessageEvent(itemRecord.sourcePlayer() + " found " + item.getDisplayName()));
                itemCategoryService.giveItem(item);
            }
        } catch (Exception e) {
            log.debug("An Unknown exception occurred", e);
        }
    }
}
