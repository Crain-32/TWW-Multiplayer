package client.service;

import client.view.events.SettingsChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettingsService {
    private final SettingsRepo settingsRepo;
    public final static String consoleIp = "ConsoleIp";
    public final static String connectionType = "ConnectionType";
    public final static String externalIntegration = "EnableExternalIntegration";

    public String getSetting(String key) {
        var settings = settingsRepo.findByName(key);
        return settings.map(Settings::getProperty).orElse(null);
    }

    public void saveSetting(String key, String val) {
        if (settingsRepo.existsByName(key)) {
            var setting = settingsRepo.findByName(key).get();
            setting.setProperty(val);
            settingsRepo.save(setting);
        } else {
            var setting = new Settings(null, key, val);
            settingsRepo.save(setting);
        }
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
