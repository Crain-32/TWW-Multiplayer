package crain.client.game;

import constants.WorldType;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class GameInfoConfig {

    private Integer worldIdAddress;
    private Integer itemIdAddress;
    private Integer worldId;
    private WorldType worldType;

    public GameInfoConfig(@Value("${game.world.id.address}")String worldIdString, @Value("${game.item.id.address}") String itemIdString) {
        worldIdAddress = Integer.parseUnsignedInt(worldIdString, 16);
        itemIdAddress = Integer.parseUnsignedInt(itemIdString, 16);
    }
}
