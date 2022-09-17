package client.communication.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import constants.WorldType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import records.ROOM;

import java.lang.reflect.Type;

@Component
@Qualifier("queueHandler")
@RequiredArgsConstructor
public class NamesQueueHandler extends AbstractQueueHandler {
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String getTopicPath() {
        return "/topic/names/";
    }

    @Override
    public WorldType supportedWorldType() {
        return WorldType.SHARED;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ROOM.PlayerRecord.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, @Nullable Object payload) {
        ROOM.PlayerRecord playerRecord = objectMapper.convertValue(payload, ROOM.PlayerRecord.class);
        if (playerRecord.worldId() != null && playerRecord.worldType() != WorldType.COOP && playerRecord.playerName() != null) {
            applicationEventPublisher.publishEvent(playerRecord);
        }
    }
}
