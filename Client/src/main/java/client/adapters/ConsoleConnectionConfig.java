package client.adapters;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * A simple POJO that contains information for Console Connections
 * IP
 * PORT
 * ConsoleType?
 * GameType? <- If you see this, shhhhhhhh. It's secret ;)
 */
@Data
@Component
public class ConsoleConnectionConfig {

    private String externalIpAddress;
    private Integer externalPort;
    // private ConsoleType consoleType;
    // private GameType gameType;
}
