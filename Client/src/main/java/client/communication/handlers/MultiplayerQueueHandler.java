package client.communication.handlers;

import client.config.GameRoomConfig;
import client.game.ItemCategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.WorldType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import records.INFO;

import java.lang.reflect.Type;
import java.util.Objects;

@Component
@Qualifier("queueHandler")
@RequiredArgsConstructor
public class MultiplayerQueueHandler extends AbstractQueueHandler {
    private final ObjectMapper objectMapper;
    private final ItemCategoryService itemCategoryService;
    private final GameRoomConfig gameRoomConfig;

    @Override
    public String getTopicPath() {
        return "/topic/multiplayer/";
    }

    @Override
    public WorldType supportedWorldType() {
        return WorldType.MULTIWORLD;
    }
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return INFO.ItemRecord.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        INFO.ItemRecord itemRecord = objectMapper.convertValue(payload, INFO.ItemRecord.class);
        if (Objects.equals(itemRecord.targetPlayerWorldId(), gameRoomConfig.getWorldId())) {
            itemCategoryService.giveItem(itemRecord.itemId());
        }
    }
}
