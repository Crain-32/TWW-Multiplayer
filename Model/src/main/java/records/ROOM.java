package records;

import constants.WorldType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * This Class holds all the Records that represent the State of the Game Room.
 */
public class ROOM {

    public record GameRoomRecord(
            @NotNull(message = "Name cannot be Null")
            String name,
            Boolean extraValidation,
            @Positive(message = "You must have at least 1 World.")
            Integer worldAmount,
            WorldType worldType,
            @Positive(message = "At least one Player is expected to Play")
            Integer playerAmount,
            List<PlayerRecord> players
    ) { }

    public record PlayerRecord(
            @Size(min = 3, max = 20, message = "Player Names must be between 3 and 20 Characters")
            String playerName,
            @Positive(message = "World ID cannot be equal to or less than 0")
            Integer worldId,
            @NotBlank(message = "World Type must be included")
            WorldType worldType,
            Boolean connected
    ) { }
}