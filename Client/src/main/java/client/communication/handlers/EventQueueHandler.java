package client.communication.handlers;

import constants.WorldType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;
import records.INFO;

@Slf4j
@Component
@RequiredArgsConstructor
@Qualifier("queueHandler")
public class EventQueueHandler extends AbstractQueueHandler<INFO.EventRecord> {

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
    protected void innerHandleFrame(StompHeaders headers, INFO.EventRecord payload) {
        log.info("Event Object received, this implementation is currently empty.");
    }
}
