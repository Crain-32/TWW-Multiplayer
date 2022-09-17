import client.communication.GameRoomApi;
import client.communication.external.MultiplayerTrackerApi;
import client.view.MainPageConstructor;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.awt.*;

@SpringBootApplication
@EntityScan(basePackages = {"client"})
@ComponentScan(basePackages = {"client"})
@EnableJpaRepositories(basePackages = {"client"})
@EnableFeignClients(clients = {GameRoomApi.class, MultiplayerTrackerApi.class})
public class Client {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(Client.class)
                .headless(false)
                .web(WebApplicationType.SERVLET) // Swap to Servlet after finishing up the GUI
                .run(args);

        EventQueue.invokeLater(() -> {
            MainPageConstructor mainPageConstructor = ctx.getBean(MainPageConstructor.class);
            mainPageConstructor.getJFrame().setVisible(true);
        });
    }
}
