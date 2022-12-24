package crain.client.service;

import constants.WorldType;
import crain.client.events.CreateMemoryAdapterEvent;
import crain.client.service.persistence.Settings;
import crain.client.service.persistence.SettingsRepo;
import crain.client.view.events.InitialSettingsEvent;
import crain.client.view.events.SettingsChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettingsService {
    private final SettingsRepo settingsRepo;
    private final ApplicationEventPublisher applicationEventPublisher;
    public final static String consoleIp = "ConsoleIp";
    public final static String connectionType = "ConnectionType";
    public final static String externalIntegration = "EnableExternalIntegration";
    public final static String gameType = "WorldType";
    public final static String roomName = "RoomName";
    public final static String password = "Password";
    public final static String worldAmount = "WorldAmount";
    public final static String worldId = "WorldId";
    public final static String playerName = "PlayerName";

    @EventListener(ApplicationStartedEvent.class)
    public void emitOldSettings() {
        try {
            var savedMemoryAdapter = settingsRepo.findByName(connectionType).orElse(new Settings(null, "connectionDefault", CreateMemoryAdapterEvent.MemoryAdapterType.DOLPHIN.toString()));
            var savedConsoleIpAddress = settingsRepo.findByName(consoleIp).orElse(new Settings(null, "consoleIpDefault", ""));
            var savedIntegration = settingsRepo.findByName(externalIntegration).orElse(new Settings(null, "defaultIntegration", Boolean.FALSE.toString()));
            var lastWorldType = settingsRepo.findByName(gameType).orElse(new Settings(null, "defaultWorldType", WorldType.MULTIWORLD.toString()));
            var lastRoomName = settingsRepo.findByName(roomName).orElse(new Settings(null, "defaultRoomName", ""));
            var lastPassword = settingsRepo.findByName(password).orElse(new Settings(null, "defaultPassword", ""));
            var lastWorldAmount = settingsRepo.findByName(worldAmount).orElse(new Settings(null, "defaultWorldAmount", Integer.toString(0)));
            var lastWorldId = settingsRepo.findByName(worldId).orElse(new Settings(null, "defaultWorldId", Integer.toString(0)));
            var lastPlayerName = settingsRepo.findByName(playerName).orElse(new Settings(null, "defaultPlayerName", ""));

            applicationEventPublisher.publishEvent(
                    new InitialSettingsEvent(
                            CreateMemoryAdapterEvent.MemoryAdapterType.valueOf(savedMemoryAdapter.getProperty()),
                            savedConsoleIpAddress.getProperty(),
                            Boolean.valueOf(savedIntegration.getProperty()),
                            WorldType.valueOf(lastWorldType.getProperty()),
                            lastRoomName.getProperty(),
                            lastPassword.getProperty(),
                            Integer.valueOf(lastWorldAmount.getProperty()),
                            Integer.valueOf(lastWorldId.getProperty()),
                            lastPlayerName.getProperty()
                    )
            );
        } catch (Exception e) {
            log.debug("Unexpected Exception happened while trying to reload settings. Deleting all Saved Settings", e);
            settingsRepo.deleteAll();
        }
    }

    public String getSetting(String key) {
        var settings = settingsRepo.findByName(key);
        return settings.map(Settings::getProperty).orElse(null);
    }

    public void saveSetting(String key, String val) {
        var dbSetting = settingsRepo.findByName(key);
        Settings setting;
        if (dbSetting.isPresent()) {
            setting = dbSetting.get();
            setting.setProperty(val);
        } else {
            setting = new Settings(null, key, val);
        }
        settingsRepo.save(setting);
    }

    @EventListener
    public void saveNewSettings(SettingsChangeEvent event) {
        if (event.enableIntegration() != null) {
            log.debug("Settings External Integration to: {}", event.enableIntegration());
            saveSetting(externalIntegration, Boolean.toString(event.enableIntegration()));
        }
        if (event.consoleIpAddress() != null) {
            log.debug("Setting Console IP to: {}", event.consoleIpAddress());
            saveSetting(consoleIp, event.consoleIpAddress());
        }
        if (event.memoryAdapterType() != null) {
            log.debug("Setting Connection Type to: {}", event.memoryAdapterType());
            saveSetting(connectionType, event.memoryAdapterType().toString());
        }
    }
}
