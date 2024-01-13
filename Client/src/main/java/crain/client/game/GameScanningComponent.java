package crain.client.game;


import crain.client.game.interfaces.MemoryAdapter;
import crain.client.game.scanners.CheckScanner;
import crain.client.view.events.GeneralMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;


@Slf4j
@Component
@RequiredArgsConstructor
public class GameScanningComponent {

    private ScheduledFuture<?> itemScanner;

    private final GameInfoConfig gameInfoConfig;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ThreadPoolTaskScheduler scheduledExecutorService;


    @Async
    @EventListener(condition = "!T(crain.client.util.NullUtil).anyNullField(event)")
    public void changeHandler(GameInterfaceEvents.MemoryHandlerEvent event) {
        log.debug("Memory Handler changed to {}", event.memoryAdapter().getClass().getSimpleName());
        MemoryAdapter handler = event.memoryAdapter();
        CheckScanner scanner = new CheckScanner(handler, applicationEventPublisher, gameInfoConfig);

        itemScanner = scheduledExecutorService.scheduleAtFixedRate(scanner, Duration.ofMillis(5000));
        scheduledExecutorService.schedule(this::itemScannerStillRunning, Instant.now().plusSeconds(300));
    }

    private void itemScannerStillRunning() {
        try {
            if (itemScanner.isDone()) {
                log.debug("Unknown Exception closed the Memory Handler", (Throwable) itemScanner.get());
                applicationEventPublisher.publishEvent(new GeneralMessageEvent("Your Connection to the Game Closed. Please Disconnect and Retry"));
                itemScanner.cancel(true);
            } else {
                scheduledExecutorService.schedule(this::itemScannerStillRunning, Instant.now().plusSeconds(120));
            }
        } catch (Exception e) {
            log.debug("Failed to Reschedule Item Scanner Validation, Attempting to Cancel the Runnable");
            itemScanner.cancel(true);
        }
    }
}
