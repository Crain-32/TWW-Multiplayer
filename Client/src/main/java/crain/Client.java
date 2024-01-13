package crain;

import crain.client.view.MainPageConstructor;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;

@SpringBootApplication
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

/**
 * Bugs
 * Create Room doesn't Populate Connect Room
 * Reloading the Client doesn't repopulate with your last connection information
 * Memory address for Item/World ID seem to not be getting cleared
 * Randomizer Header Struct isn't Integrated (I think, need to triple check that)
 *
 * Work
 * Swap Event Listener Checks from Functions to EventListener.condition :check:
 * Push Statuses into Beans to work with the above ^ (some properties have be located)
 * Tack on some more @Async and enable Virtual Threads :check:
 * Revalidate Console Socket Handling
 */