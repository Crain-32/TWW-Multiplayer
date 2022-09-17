package client.communication.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import constants.WorldType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import records.INFO;

import java.lang.reflect.Type;

@Component
@Qualifier("queueHandler")
@RequiredArgsConstructor
public class EventQueueHandler extends AbstractQueueHandler {

    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String getTopicPath() {
        return "/topic/event/";
    }

    @Override
    public WorldType supportedWorldType() {
        return WorldType.COOP;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return INFO.EventRecord.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, @Nullable Object payload) {
        INFO.EventRecord record = objectMapper.convertValue(payload, INFO.EventRecord.class);

    }
}
