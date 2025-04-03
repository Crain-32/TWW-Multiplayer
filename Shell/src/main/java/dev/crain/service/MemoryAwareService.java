package dev.crain.service;

import dev.crain.exceptions.memory.MemoryAdapterDisconnectException;
import dev.crain.exceptions.memory.MemoryHandlerException;
import dev.crain.exceptions.memory.MissingMemoryAdapterException;
import dev.crain.game.GameInterfaceEvents;
import dev.crain.game.interfaces.MemoryAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * Abstract class that gives the extender the ability to access/verify the MemoryAdaptor without
 * having to worry about syncing the state.
 *
 * $.link.health=5
 */
@Slf4j
public abstract class MemoryAwareService {


    private MemoryAdapter memoryAdapter = null;

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

    private void verifyHandler() throws MissingMemoryAdapterException, MemoryAdapterDisconnectException {
        if (memoryAdapter == null) {
            throw new MissingMemoryAdapterException("No Memory Adapter could be found");
        }
        log.trace("Current Memory Adapter: {}", getClassName());
        if (!memoryAdapter.isConnected()) {
            throw new MemoryAdapterDisconnectException("Memory Adapter is not connected");
        }
    }

    protected final MemoryAdapter getMemoryAdapter() throws MemoryHandlerException {
        verifyHandler();
        return memoryAdapter;
    }

    protected abstract String getClassName();
}
