package client.communication.handlers;

import client.view.events.GeneralMessageEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.WorldType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import records.ROOM;

import java.lang.reflect.Type;

@Slf4j
@Component
@Qualifier("queueHandler")
@RequiredArgsConstructor
public class GeneralQueueHandler extends AbstractQueueHandler {
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String getTopicPath() {
        return "/topic/general/";
    }

    @Override
    public WorldType supportedWorldType() {
        return WorldType.SHARED;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ROOM.MessageRecord.class;
    }
    @Override
    public void handleFrame(StompHeaders headers, @Nullable Object payload) {
        log.debug("Handling Frame in General Queue Handler");
        var generalMessage = objectMapper.convertValue(payload, ROOM.MessageRecord.class);
        if (StringUtils.isNotBlank(generalMessage.message())) {
            applicationEventPublisher.publishEvent(new GeneralMessageEvent(generalMessage.message()));
        }
    }
}
