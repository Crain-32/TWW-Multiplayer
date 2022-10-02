package crain.model.records;

import constants.WorldType;

import java.util.List;

public class DETAIL {

    public record GameRoom(
            Long id,
            String name,
            Boolean extraValidation,
            Integer worldAmount,
            Integer connectedPlayerCount,
            WorldType worldType,
            List<Player> players,
            Boolean tournament,
            String creationTimestamp) {}

    public record Player(
            Long id,
            String playerName,
            WorldType worldType,
            Boolean connected,
            List<Integer> queuedItems,
            String lastInteractionDate
    ) {}
}
