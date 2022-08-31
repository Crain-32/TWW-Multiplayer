package client.events;

import client.game.data.ItemInfo;
import lombok.Data;

@Data
public class PushItemRecord {
    private ItemInfo info;
}
