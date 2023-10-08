package crain.model.tournament;

import java.util.List;

public record TournamentPlayer(
        String name,
        Integer worldId,
        List<Integer> items
) {
}
