package crain.client.communication.handlers;

import constants.WorldType;
import crain.client.config.GameRoomConfig;
import crain.client.service.ItemCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import records.INFO;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Qualifier("queueHandler")
public class MultiplayerQueueHandler extends AbstractQueueHandler<INFO.ItemRecord> {

    private final GameRoomConfig gameRoomConfig;
    private final ItemCategoryService itemCategoryService;
    private final ApplicationEventPublisher applicationEventPublisher;

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
