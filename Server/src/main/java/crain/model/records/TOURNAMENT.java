package crain.model.records;

import crain.model.domain.GameRoom;
import crain.model.domain.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * We're going to hold the Records for the Eventual Tournament Support in here.
 * The plan is to have an Endpoint that Tournament Planners can subscribe to in order
 * to have Auto-Tracking for Coop/Multiworld.
 */
public class TOURNAMENT {
    public record TournamentPlayerRecord(
            String name,
            Integer worldId,
            List<Integer> items
    ) {
        public static TournamentPlayerRecord fromPlayer(Player player) {
            return new TournamentPlayerRecord(
                    player.getPlayerName(),
                    player.getWorldId(),
                    player.getItems()
            );
        }
    }

    public record TournamentRecord(
            String gameRoom,
            List<TournamentPlayerRecord> players
    ) {
        public static TournamentRecord fromGameRoom(GameRoom sourceGameRoom) {
            return new TournamentRecord(
                    sourceGameRoom.getName(),
                    sourceGameRoom.getPlayers().stream()
                            .map(TournamentPlayerRecord::fromPlayer)
                            .collect(Collectors.toList()));
        }
    }
}
