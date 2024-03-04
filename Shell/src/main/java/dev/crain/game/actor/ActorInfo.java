package dev.crain.game.actor;

import java.util.List;

public record ActorInfo(String englishName, List<String> actorNames, String archiveName, Integer actorId,
                        String relName, ActorType actorType) {
}
