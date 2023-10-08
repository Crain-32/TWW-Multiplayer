package crain.model.tournament;

import java.util.List;

public record TournamentRoom(
        String gameRoom,
        List<TournamentPlayer> players
) {
}
