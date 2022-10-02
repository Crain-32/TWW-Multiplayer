package client.communication.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import constants.WorldType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import records.ROOM;

@Component
@Qualifier("queueHandler")
public class NamesQueueHandler extends AbstractQueueHandler<ROOM.PlayerRecord> {
    private final ApplicationEventPublisher applicationEventPublisher;

    public NamesQueueHandler(ApplicationEventPublisher applicationEventPublisher, ObjectMapper objectMapper) {
        super(objectMapper);
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public String getTopicPath() {
        return "/topic/names/";
    }

    @Override
    public WorldType supportedWorldType() {
        return WorldType.SHARED;
    }

    @Override
    public void innerHandleFrame(StompHeaders headers, ROOM.PlayerRecord playerRecord) {
        if (playerRecord.worldId() != null && playerRecord.worldType() != WorldType.COOP && playerRecord.playerName() != null) {
            applicationEventPublisher.publishEvent(playerRecord);
        }
    }
}
