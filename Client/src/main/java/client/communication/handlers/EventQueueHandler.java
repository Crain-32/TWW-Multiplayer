package client.communication.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import records.INFO;

@Component
public class EventQueueHandler extends StompSessionHandlerAdapter {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleFrame(StompHeaders headers, @Nullable Object payload) {
        INFO.EventRecord record = objectMapper.convertValue(payload, INFO.EventRecord.class);
    }
}
