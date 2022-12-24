package crain.client.communication.handlers;

import constants.WorldType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import records.ROOM;

@Component
@RequiredArgsConstructor
@Qualifier("queueHandler")
public class NamesQueueHandler extends AbstractQueueHandler<ROOM.PlayerRecord> {
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
    public void innerHandleFrame(StompHeaders headers, ROOM.PlayerRecord playerRecord) {
        if (playerRecord.worldId() != null && playerRecord.worldType() != WorldType.COOP && playerRecord.playerName() != null) {
            applicationEventPublisher.publishEvent(playerRecord);
        }
    }
}
