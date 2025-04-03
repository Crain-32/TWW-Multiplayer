package records;

import constants.WorldType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

/**
 * This Class holds all the Records that represent the State of the Game Room.
 */
public class ROOM {

    @Builder
    public record GameRoomRecord(
            @NotNull(message = "Name cannot be Null")
            String name,
            boolean extraValidation,
            @Positive(message = "You must have at least 1 World.")
            Integer worldAmount,
            @NotNull(message = "A world type must be provided")
            WorldType worldType,
            @Positive(message = "At least one Player is expected to Play")
            int playerAmount,
            List<PlayerRecord> players
    ) {
    }

    @Builder
    public record PlayerRecord(
            @Size(min = 3, max = 20, message = "Player Names must be between 3 and 20 Characters")
            String playerName,
            @Positive(message = "World ID cannot be equal to or less than 0")
            int worldId,
            @NotNull(message = "World Type must be included")
            WorldType worldType,
            Boolean connected
    ) {
    }

    @Builder
    public record MessageRecord(
            @Size(min = 3, max = 120, message = "Max Message Size is 120 Characters")
            String message
    ) {
    }

    @Builder
    public record ErrorRecord(
            @NotNull
            String error
    ) {
    }
}
