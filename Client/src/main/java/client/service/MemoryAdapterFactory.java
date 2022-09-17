package client.service;

import client.adapters.ConsoleConnectionConfig;
import client.adapters.DolphinAdapter;
import client.adapters.NintendontAdapter;
import client.events.CreateMemoryAdapterEvent;
import client.game.GameInterfaceEvents;
import client.view.events.GeneralMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemoryAdapterFactory {

    private final BeanFactory beanFactory;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ConsoleConnectionConfig consoleConnectionConfig;
    private final SettingsService settingsService;


    @Async
    @EventListener
    public void createMemoryAdapterEventHandler(CreateMemoryAdapterEvent event) {
        try {
            log.debug("Creating a new Memory Adapter of Type: " + event.getMemoryAdapterType());
            switch (event.getMemoryAdapterType()) {
                case DOLPHIN -> createDolphinMemoryAdapter();
                case NINTENDONT -> createNintendontAdapter();
                default -> throw new IllegalStateException("This Exception should not be reachable!");
            }
        } catch (Exception e) {
            log.debug("Failed to create Memory Adapter with the following Exception", e);
            applicationEventPublisher.publishEvent(new GeneralMessageEvent("Failed to create Memory Adapter!\n" + e.getMessage()));
        }
    }

    @SneakyThrows
    private void createDolphinMemoryAdapter() {
        DolphinAdapter adapter = beanFactory.getBean(DolphinAdapter.class);
        adapter.connect();
        applicationEventPublisher.publishEvent(new GameInterfaceEvents.MemoryHandlerEvent(adapter));
    }

    @SneakyThrows
    private void createNintendontAdapter() {
        consoleConnectionConfig.setExternalIpAddress(settingsService.getSetting(SettingsService.consoleIp));
        NintendontAdapter adapter = beanFactory.getBean(NintendontAdapter.class);
        adapter.connect();
        applicationEventPublisher.publishEvent(new GameInterfaceEvents.MemoryHandlerEvent(adapter));
    }
}
