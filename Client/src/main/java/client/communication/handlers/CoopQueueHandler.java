package client.communication.handlers;

import client.communication.GameRoomConfig;
import client.game.ItemCategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import records.INFO;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CoopQueueHandler extends StompSessionHandlerAdapter {

    private final ObjectMapper objectMapper;
    //Improper, we need to route this through another Service First in order to accomplish the following,
    // Queue the Item if we can't get it, or fail to give it.
    // Display the Information to the User
    // Logging?
    // Manage the GameHandler.
    private final ItemCategoryService itemCategoryService;
    private final GameRoomConfig gameRoomConfig;

    @Override
    public void handleFrame(StompHeaders headers, @Nullable Object payload) {
        INFO.CoopItemRecord itemRecord = objectMapper.convertValue(payload, INFO.CoopItemRecord.class);
        if (!Objects.equals(itemRecord.sourcePlayer(), gameRoomConfig.getPlayerName())) {
            itemCategoryService.giveItem(itemRecord.itemId());
        }
    }
}
