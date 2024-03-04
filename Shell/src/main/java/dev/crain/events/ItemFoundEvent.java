package dev.crain.events;

import dev.crain.game.data.ItemInfo;

public record ItemFoundEvent(ItemInfo info, Integer worldId, String checkName) { }