package crain.client.game.scanners;

import crain.client.game.data.StageFlagInfo;
import crain.client.game.interfaces.MemoryAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Objects;

/**
 * These Scanners should specific Scan for NON-LOCATION BASED FLAGS
 * Examples
 * - Dungeon Flags like Doors
 * - Revealing Chests
 *
 * Examples of what you don't want to Scan
 * - Chests
 * - Boss Items
 * - Blue Chus
 */
@Slf4j
@RequiredArgsConstructor
public class StageFlagScanner implements Runnable {

    private final MemoryAdapter memoryAdapter;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void run() {
        if (Objects.isNull(memoryAdapter) || !memoryAdapter.isConnected()) {
            log.debug("No Handler Found, or Handler was not Connected");
            return;
        }
        String eventState = memoryAdapter.readString(StageFlagInfo.CURRENT.getMemoryLocation(), 26);
    }
}
