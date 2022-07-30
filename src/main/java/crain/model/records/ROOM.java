package crain.model.records;

import crain.model.constants.WorldType;
import crain.model.domain.GameRoom;
import crain.model.domain.Player;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.*;
import java.util.List;

public class ROOM {

    public record GameRoomRecord(
            Long id,
            @NotNull(message = "Name cannot be Null")
            String name,
            Boolean extraValidation,
            Integer worldAmount,
            Integer playerAmount,
            List<PlayerRecord> players
    ) {
        public static GameRoomRecord fromEntity(GameRoom gameRoom) {
            List<PlayerRecord> playerRecordList = gameRoom.getPlayers().stream()
                    .map(PlayerRecord::fromEntity).toList();

            return new GameRoomRecord(
                    gameRoom.getId(),
                    gameRoom.getName(),
                    StringUtils.isNoneBlank(gameRoom.getPassword()),
                    gameRoom.getWorldAmount(),
                    gameRoom.getConnectedPlayerCount(),
                    playerRecordList
            );
        }
    }

    public record PlayerRecord(
            @Null(message = "Requests cannot set this")
            Long id,
            @Size(min = 3, max = 20, message = "Player Names must be between 3 and 20 Characters")
            String playerName,
            @Positive(message = "World ID cannot be equal to or less than 0")
            Integer worldId,
            @NotBlank(message = "World Type must be included")
            WorldType worldType,
            @Null(message = "Requests cannot set this")
            Boolean connected
    ) {
        public static PlayerRecord fromEntity(@NonNull Player player) {
            return new PlayerRecord(
                    player.getId(),
                    player.getPlayerName(),
                    player.getWorldId(),
                    player.getWorldType(),
                    player.getConnected());
        }

    }
}
