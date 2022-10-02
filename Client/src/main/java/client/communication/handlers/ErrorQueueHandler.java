package client.communication.handlers;

import client.view.events.GeneralMessageEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.WorldType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import records.ROOM;

@Component
@Qualifier("queueHandler")
public class ErrorQueueHandler extends AbstractQueueHandler<ROOM.ErrorRecord> {

    private final ApplicationEventPublisher applicationEventPublisher;

    public ErrorQueueHandler(ApplicationEventPublisher applicationEventPublisher, ObjectMapper objectMapper) {
        super(objectMapper);
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public String getTopicPath() {
        return "/topic/error/";
    }

    @Override
    public WorldType supportedWorldType() {
        return WorldType.SHARED;
    }

    protected void innerHandleFrame(StompHeaders headers, @Nullable ROOM.ErrorRecord errorMessage) {
        applicationEventPublisher.publishEvent(new GeneralMessageEvent("The Server has sent an Error: " + errorMessage.error()));
    }
}
