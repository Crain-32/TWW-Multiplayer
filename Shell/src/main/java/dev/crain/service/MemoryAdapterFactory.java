package dev.crain.service;

import dev.crain.adapters.ConsoleConnectionConfig;
import dev.crain.adapters.DolphinAdapter;
import dev.crain.adapters.NintendontAdapter;
import dev.crain.exceptions.memory.MissingGameAdapterException;
import dev.crain.game.GameInterfaceEvents;
import dev.crain.game.interfaces.MemoryAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.shell.command.annotation.Command;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Command(command = {"game", "connect"})
public class MemoryAdapterFactory {

    private final BeanFactory beanFactory;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ConsoleConnectionConfig consoleConnectionConfig;
    private final SettingsService settingsService;
    private static volatile MemoryAdapter referenceAdapter;

    public MemoryAdapterFactory(BeanFactory beanFactory, ApplicationEventPublisher applicationEventPublisher, ConsoleConnectionConfig consoleConnectionConfig, SettingsService settingsService) {
        this.beanFactory = beanFactory;
        this.applicationEventPublisher = applicationEventPublisher;
        this.consoleConnectionConfig = consoleConnectionConfig;
        this.settingsService = settingsService;
    }


    @Command(command = "dolphin")
    public String createDolphinMemoryAdapter() throws MissingGameAdapterException {
        if (referenceAdapter != null && referenceAdapter instanceof DolphinAdapter) {
            return "Already hooked to Dolphin, maybe you mean game connect refresh?";
        }
        DolphinAdapter adapter = beanFactory.getBean(DolphinAdapter.class);
        adapter.connect();
        referenceAdapter = adapter;
        applicationEventPublisher.publishEvent(new GameInterfaceEvents.MemoryHandlerEvent(adapter));
        return "Successfully hooked into Dolphin";
    }

    @Command(command = "console")
    public String createNintendontAdapter() {
        if (referenceAdapter != null && referenceAdapter instanceof NintendontAdapter) {
            return "Already hooked to your Console, maybe you mean game connect refresh?";
        }
        consoleConnectionConfig.setExternalIpAddress(settingsService.getSetting(SettingsService.consoleIp));
        NintendontAdapter adapter = beanFactory.getBean(NintendontAdapter.class);
        adapter.connect();
        referenceAdapter = adapter;
        applicationEventPublisher.publishEvent(new GameInterfaceEvents.MemoryHandlerEvent(adapter));
        return "Successfully hooked into your Console";
    }

    @Command(command = "refresh")
    public String refreshConnection() throws MissingGameAdapterException {
        referenceAdapter.disconnect();
        referenceAdapter.connect();
        return "Connection Refreshed";
    }
}
