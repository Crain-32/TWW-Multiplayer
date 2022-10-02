package client.communication.handlers;

import client.view.events.GeneralMessageEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import constants.WorldType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import records.ROOM;

@Slf4j
@Component
@Qualifier("queueHandler")
public class GeneralQueueHandler extends AbstractQueueHandler<ROOM.MessageRecord> {
    private final ApplicationEventPublisher applicationEventPublisher;

    public GeneralQueueHandler(ApplicationEventPublisher applicationEventPublisher, ObjectMapper objectMapper) {
        super(objectMapper);
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public String getTopicPath() {
        return "/topic/general/";
    }

    @Override
    public WorldType supportedWorldType() {
        return WorldType.SHARED;
    }

    @Override
    protected void innerHandleFrame(StompHeaders headers, ROOM.MessageRecord generalMessage) {
        if (StringUtils.isNotBlank(generalMessage.message())) {
            applicationEventPublisher.publishEvent(new GeneralMessageEvent(generalMessage.message()));
        }
    }
}
