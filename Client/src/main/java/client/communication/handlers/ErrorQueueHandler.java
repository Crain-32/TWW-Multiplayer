package client.communication.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ErrorQueueHandler extends StompSessionHandlerAdapter {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleFrame(StompHeaders headers, @Nullable Object payload) {
        String errorMessage = objectMapper.convertValue(payload, String.class);
    }
}
