package crain.client.events;

import crain.client.game.data.StoryFlagInfo;
import lombok.Data;

@Data
public class GameEventEvent {

    // Since we'll be emitting and consuming this,
    // We want a want to know if we're emitting.
    // "true" should be consumed, "false" should
    // be sent out to the server.
    private Boolean consume;
    private StoryFlagInfo eventType;
    private Integer eventIndex;
    private Boolean enabled;
}
