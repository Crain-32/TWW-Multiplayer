package client.communication.handlers;

import client.config.GameRoomConfig;
import client.game.ItemCategoryService;
import client.game.data.ItemInfo;
import client.view.events.GeneralMessageEvent;
import constants.WorldType;
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

    protected void innerHandleFrame(StompHeaders headers, INFO.CoopItemRecord itemRecord) {
        if (!Objects.equals(itemRecord.sourcePlayer(), gameRoomConfig.getPlayerName())) {
            ItemInfo item = ItemInfo.getInfoByItemId(itemRecord.itemId());
            applicationEventPublisher.publishEvent(new GeneralMessageEvent(itemRecord.sourcePlayer() + " found " + item.getDisplayName()));
            itemCategoryService.giveItem(item);
        }
    }
}

