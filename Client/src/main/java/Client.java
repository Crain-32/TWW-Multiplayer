import client.communication.TestClient;
import client.view.MainPageConstructor;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.awt.*;

@SpringBootApplication
@ComponentScan(basePackages = {"client"})
@EnableFeignClients(clients = {TestClient.class})
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
