package crain.client.communication.handlers;

import constants.WorldType;
import crain.client.config.GameRoomConfig;
import crain.client.game.data.ItemInfo;
import crain.client.service.ItemCategoryService;
import crain.client.view.events.GeneralMessageEvent;
import jakarta.validation.constraints.NotNull;
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
public class CoopQueueHandler extends AbstractQueueHandler<INFO.CoopItemRecord> {

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

    protected void innerHandleFrame(@NotNull StompHeaders headers, INFO.@NotNull CoopItemRecord itemRecord) {
        if (!Objects.equals(itemRecord.sourcePlayer(), gameRoomConfig.getPlayerName())) {
            ItemInfo item = ItemInfo.getInfoByItemId(itemRecord.itemId());
            applicationEventPublisher.publishEvent(new GeneralMessageEvent(itemRecord.sourcePlayer() + " found " + item.getDisplayName()));
            itemCategoryService.giveItem(item);
        }
    }
}

