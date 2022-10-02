package client.communication.handlers;

import client.config.GameRoomConfig;
import client.game.ItemCategoryService;
import client.game.data.ItemInfo;
import client.view.events.GeneralMessageEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.WorldType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import records.INFO;

import java.util.Objects;

@Slf4j
@Component
@Qualifier("queueHandler")
public class CoopQueueHandler extends AbstractQueueHandler<INFO.CoopItemRecord> {

    private final ItemCategoryService itemCategoryService;
    private final GameRoomConfig gameRoomConfig;
    private final ApplicationEventPublisher applicationEventPublisher;

    public CoopQueueHandler(ItemCategoryService itemCategoryService, GameRoomConfig gameRoomConfig, ApplicationEventPublisher applicationEventPublisher, ObjectMapper objectMapper) {
        super(objectMapper);
        this.itemCategoryService = itemCategoryService;
        this.gameRoomConfig = gameRoomConfig;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public String getTopicPath() {
        return "/topic/coop/";
    }

    @Override
    public WorldType supportedWorldType() {
        return WorldType.COOP;
    }

    protected void innerHandleFrame(StompHeaders headers, INFO.CoopItemRecord itemRecord) {
        if (!Objects.equals(itemRecord.sourcePlayer(), gameRoomConfig.getPlayerName())) {
            log.info(itemRecord.toString());
            ItemInfo item = ItemInfo.getInfoByItemId(itemRecord.itemId());
            applicationEventPublisher.publishEvent(new GeneralMessageEvent(itemRecord.sourcePlayer() + " found " + item.getDisplayName()));
            itemCategoryService.giveItem(item);
        }
    }
}

