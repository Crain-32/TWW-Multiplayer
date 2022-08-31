package client.communication.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import records.ROOM;

@Component
public class NamesQueueHandler extends StompSessionHandlerAdapter {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleFrame(StompHeaders headers, @Nullable Object payload) {
        ROOM.PlayerRecord playerRecord = objectMapper.convertValue(payload, ROOM.PlayerRecord.class);
    }
}
