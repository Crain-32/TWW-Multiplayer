package client.communication.handlers;

import client.communication.GameRoomConfig;
import client.game.ItemCategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import records.INFO;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MultiplayerQueueHandler extends StompSessionHandlerAdapter {
    private final ObjectMapper objectMapper;
    private final ItemCategoryService itemCategoryService;
    private final GameRoomConfig gameRoomConfig;

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        INFO.ItemRecord itemRecord = objectMapper.convertValue(payload, INFO.ItemRecord.class);
        if (Objects.equals(itemRecord.targetPlayerWorldId(), gameRoomConfig.getWorldId())) {
            itemCategoryService.giveItem(itemRecord.itemId());
        }
    }
}
