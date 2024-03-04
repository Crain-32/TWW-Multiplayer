package dev.crain.game.rel;

import dev.crain.game.data.ReleaseVersion;

import java.util.Map;


public record RelInfo(ReleaseVersion version, Map<String, Integer> symbolOffset) {
}
