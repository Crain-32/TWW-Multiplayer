package client.communication.handlers;

import constants.WorldType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

@Slf4j
public abstract class AbstractQueueHandler extends StompSessionHandlerAdapter {

    /**
     * Get the Intended /topic/...../GameRoom Path This Handler Supports
     */
    public abstract String getTopicPath();

    public abstract WorldType supportedWorldType();

    @Override
    public void handleException(StompSession session, @Nullable StompCommand command,
                                StompHeaders headers, byte[] payload, Throwable exception) {
        log.info("Unhandled Exception Targeting:" + headers.getDestination());
        log.info("Unhandled Exception Received", exception);
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.info("A Transport Error Occurred", exception);
    }
}
