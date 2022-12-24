package crain.client.config;

import crain.client.communication.GameRoomApi;
import crain.client.communication.external.MultiplayerTrackerApi;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "crain.client")
@EnableJpaRepositories(basePackages = "crain.client.service")
@EnableFeignClients(clients = {GameRoomApi.class, MultiplayerTrackerApi.class})
public class MiscConfig {
}
