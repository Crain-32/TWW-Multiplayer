package dev.crain.service;

import dev.crain.service.persistence.Settings;
import dev.crain.service.persistence.SettingsRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettingsService {
    private final SettingsRepo settingsRepo;
    private final ApplicationEventPublisher applicationEventPublisher;
    public final static String consoleIp = "ConsoleIp";
    public final static String connectionType = "ConnectionType";
    public final static String externalIntegration = "EnableExternalIntegration";
    public final static String EXTERNAL_TRACKER_WEBSITE = "ExternalTrackerWebsite";
    public final static String BROKER_SERVER_DOMAIN = "BrokerServerDomain";
    public final static String BROKER_SERVER_PORT = "BrokerServerPort";
    public final static String gameType = "WorldType";
    public final static String roomName = "RoomName";
    public final static String password = "Password";
    public final static String worldAmount = "WorldAmount";
    public final static String worldId = "WorldId";
    public final static String playerName = "PlayerName";

    @Value("${multiplayer.server.url}")
    protected String brokerServerUrlDefault;

    @Value("${multiplayer.server.port}")
    protected String brokerServerPortDefault;

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

    public String getBrokerServerURL() {
        var domainName = settingsRepo.findByName(BROKER_SERVER_DOMAIN);
        if (domainName.isEmpty()) {
            var defaultUrl = settingsRepo.save(new Settings(null, BROKER_SERVER_DOMAIN, brokerServerUrlDefault));
            domainName = Optional.of(defaultUrl);
        }
        var brokerPort = settingsRepo.findByName(BROKER_SERVER_PORT);
        if (brokerPort.isEmpty()) {
            var defaultPort = settingsRepo.save(new Settings(null, BROKER_SERVER_PORT, brokerServerPortDefault));
            brokerPort = Optional.of(defaultPort);
        }
        // We'll still use the defaults as a fallback, but we want to reference the saved properties first.
        return String.format("%s:%s",
                domainName.map(Settings::getProperty).orElse(brokerServerUrlDefault),
                brokerPort.map(Settings::getProperty).orElse(brokerServerPortDefault)
        );
    }
}
