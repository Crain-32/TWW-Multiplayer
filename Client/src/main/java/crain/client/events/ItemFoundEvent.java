package crain.client.events;

import crain.client.game.data.ItemInfo;

public record ItemFoundEvent(ItemInfo info, Integer worldId, String checkName) { }