package client.communication.handlers;

import client.view.events.GeneralMessageEvent;
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
public class ErrorQueueHandler extends AbstractQueueHandler<ROOM.ErrorRecord> {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String getTopicPath() {
        return "/topic/error/";
    }

    @Override
    public WorldType supportedWorldType() {
        return WorldType.SHARED;
    }

    protected void innerHandleFrame(StompHeaders headers, ROOM.ErrorRecord errorMessage) {
        applicationEventPublisher.publishEvent(new GeneralMessageEvent("The Server has sent an Error: " + errorMessage.error()));
    }
}
