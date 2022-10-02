package client.events;

import client.game.data.ItemInfo;

public record ItemFoundEvent(ItemInfo info, Integer worldId) {

}
