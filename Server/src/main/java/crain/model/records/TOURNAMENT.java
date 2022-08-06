package crain.model.records;

import crain.model.domain.GameRoom;
import crain.model.domain.Player;

import java.util.List;
import java.util.stream.Collectors;


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
