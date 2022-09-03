package client.events;

import client.game.data.EventInfo;
import lombok.Data;

@Data
public class GameEventEvent {

    // Since we'll be emitting and consuming this,
    // We want a want to know if we're emitting.
    // "true" should be consumed, "false" should
    // be sent out to the server.
    private Boolean consume;
    private EventInfo eventType;
    private Integer eventIndex;
    private Boolean enabled;
}
