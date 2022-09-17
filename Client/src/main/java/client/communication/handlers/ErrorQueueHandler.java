package client.communication.handlers;

import client.view.events.GeneralMessageEvent;
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
public class ErrorQueueHandler extends AbstractQueueHandler {

    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String getTopicPath() {
        return "/topic/error/";
    }

    @Override
    public WorldType supportedWorldType() {
        return WorldType.SHARED;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ROOM.ErrorRecord.class;
    }
    @Override
    public void handleFrame(StompHeaders headers, @Nullable Object payload) {
        var errorMessage = objectMapper.convertValue(payload, ROOM.ErrorRecord.class);
        applicationEventPublisher.publishEvent(new GeneralMessageEvent("The Server has sent an Error: " + errorMessage.error()));
    }
}
