package crain.client.communication.handlers;

import constants.WorldType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Slf4j
public abstract class AbstractQueueHandler<T> extends StompSessionHandlerAdapter {

    /**
     * Get the Intended /topic/...../GameRoom Path This Handler Supports
     */
    public abstract String getTopicPath();

    public abstract WorldType supportedWorldType();

    @Override
    public void handleException(@Nullable StompSession session, @Nullable StompCommand command,
                                StompHeaders headers, @Nullable byte[] payload, @Nullable Throwable exception) {
        log.info("Unhandled Exception Targeting:" + headers.getDestination());
        log.info("Unhandled Exception Received", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void handleFrame(StompHeaders headers, @Nullable Object payload) {
        if (payload == null) {
            log.debug("Payload was null");
            return;
        }
        log.debug("Received a frame for the following Destination: {}", headers.getDestination());
        log.debug("Object Received: {}", payload);

        try {
            innerHandleFrame(headers, (T) payload);
        } catch (Exception e) {
            log.error("An unexpected Exception occurred", e);
        }
    }

    protected abstract void innerHandleFrame(StompHeaders headers, @NotNull T object);


    /**
     * This implementation is empty.
     */
    @Override
    public void handleTransportError(@NonNull StompSession session, @NonNull Throwable exception) {
        log.error("A Transport Error Occurred", exception);
    }
}
