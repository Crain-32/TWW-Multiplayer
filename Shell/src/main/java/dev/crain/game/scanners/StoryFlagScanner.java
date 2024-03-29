package dev.crain.game.scanners;

import dev.crain.events.GameFlagToggleEvent;
import dev.crain.exceptions.memory.MemoryHandlerException;
import dev.crain.exceptions.memory.MissingMemoryAdapterException;
import dev.crain.game.data.StoryFlagInfo;
import dev.crain.game.interfaces.MemoryAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Map;
import java.util.Objects;

/**
 * These Scanners should specific Scan for NON-LOCATION BASED FLAGS
 * Examples
 * - Starting to talk to Ms Marie
 * - Starting Tag
 * - Talking to Lenzo/Triggers
 * <p>
 * Examples of what you don't want to Scan
 * - Zunari
 * - Sploosh Kaboom
 */
@Slf4j
@RequiredArgsConstructor
public class StoryFlagScanner implements Runnable {

    private final MemoryAdapter memoryAdapter;
    private final Map<StoryFlagInfo, Boolean> toCheck;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void run() {
        for (var storyFlag : toCheck.keySet()) {
            try {
                final boolean scannedState = checkStoryFlag(storyFlag);
                if (scannedState != toCheck.get(storyFlag)) {
                    applicationEventPublisher.publishEvent(new GameFlagToggleEvent(GameFlagToggleEvent.FlagType.STORY, storyFlag.getMemoryAddress(), null, storyFlag.getBitIndex(), scannedState));
                }
            } catch (MemoryHandlerException exception) {
                log.debug("Failed to check Flag State", exception);
            }
        }
    }

    private boolean checkStoryFlag(StoryFlagInfo info) throws MissingMemoryAdapterException {
        if (Objects.isNull(memoryAdapter) || !memoryAdapter.isConnected()) {
            throw new MissingMemoryAdapterException("No Handler Found, or Handler was not Connected");
        }
        final byte currState = memoryAdapter.readByte(info.getMemoryAddress());
        final byte mask = (byte) (1 << info.getBitIndex());
        log.trace("Current Value {} and applied Mask {}", currState, mask);

        return ((currState & mask) != 0);
    }
}
