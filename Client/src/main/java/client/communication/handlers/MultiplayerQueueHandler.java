package client.communication.handlers;

import client.config.GameRoomConfig;
import client.game.ItemCategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.WorldType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import records.INFO;

import java.util.Objects;

@Component
@Qualifier("queueHandler")
public class MultiplayerQueueHandler extends AbstractQueueHandler<INFO.ItemRecord> {
    private final ItemCategoryService itemCategoryService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final GameRoomConfig gameRoomConfig;

    public MultiplayerQueueHandler(ItemCategoryService itemCategoryService, ApplicationEventPublisher applicationEventPublisher, GameRoomConfig gameRoomConfig, ObjectMapper objectMapper) {
        super(objectMapper);
        this.itemCategoryService = itemCategoryService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.gameRoomConfig = gameRoomConfig;
    }

    @Override
    public String getTopicPath() {
        return "/topic/multiplayer/";
    }

    @Override
    public WorldType supportedWorldType() {
        return WorldType.SHARED;
    }

    @Override
    protected void innerHandleFrame(StompHeaders headers, INFO.ItemRecord itemRecord) {
        if (Objects.equals(itemRecord.targetPlayerWorldId(), gameRoomConfig.getWorldId())) {
            applicationEventPublisher.publishEvent(itemRecord);
            itemCategoryService.giveItem(itemRecord.itemId());
        }
    }
}
