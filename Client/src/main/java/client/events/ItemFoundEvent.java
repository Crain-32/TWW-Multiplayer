package client.events;

import client.game.data.ItemInfo;
import lombok.Data;

@Data
public class ItemFoundEvent {
    private ItemInfo info;
    private Integer worldId;
}
