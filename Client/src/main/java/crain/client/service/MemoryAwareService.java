package crain.client.service;

import crain.client.exceptions.MissingMemoryAdapterException;
import crain.client.game.GameInterfaceEvents;
import crain.client.game.interfaces.MemoryAdapter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * Abstract class that gives the extender the ability to access/verify the MemoryAdaptor without
 * having to worry about syncing the state.
 */
@Slf4j
public abstract class MemoryAwareService {

    @Getter
    protected MemoryAdapter memoryAdapter = null;

    @Async
    @EventListener
    public void setMemoryAdapterEvent(GameInterfaceEvents.MemoryHandlerEvent event) {
        log.debug(
                "{} Updated the Memory Adapter to {}",
                getClassName(),
                event.memoryAdapter().getClass().getSimpleName()
        );
        this.memoryAdapter = event.memoryAdapter();
    }

    protected void verifyHandler() throws MissingMemoryAdapterException, IllegalStateException {
        if (memoryAdapter == null) {
            throw new MissingMemoryAdapterException("No Memory Adapter could be found");
        }
        if (log.isTraceEnabled()) {
            log.trace("Current Memory Adapter: {}", getClassName());
        }
        if (!memoryAdapter.isConnected()) {
            throw new IllegalStateException("Memory Adapter is not connected");
        }
    }

    protected abstract String getClassName();
}
