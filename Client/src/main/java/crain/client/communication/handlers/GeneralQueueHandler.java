package crain.client.communication.handlers;

import constants.WorldType;
import crain.client.view.events.GeneralMessageEvent;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import records.ROOM;

@Slf4j
@Component
@RequiredArgsConstructor
@Qualifier("queueHandler")
public class GeneralQueueHandler extends AbstractQueueHandler<ROOM.MessageRecord> {
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
    protected void innerHandleFrame(@NotNull StompHeaders headers, ROOM.@NotNull MessageRecord generalMessage) {
        if (StringUtils.isNotBlank(generalMessage.message())) {
            applicationEventPublisher.publishEvent(new GeneralMessageEvent(generalMessage.message()));
        }
    }
}
