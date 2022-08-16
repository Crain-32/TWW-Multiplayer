package records;

import constants.WorldType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * This Class holds all the records for the STOMP/ReST API communication.
 */
public class INFO {
    /**
     * This Model is Subject to Deprecation. COOP Handling is not finalized.
     * @param sourcePlayer The Player who sent the Item into the Server.
     * @param itemId The ID of the Item found.
     */
    public record CoopItemRecord(
            @NotNull(message = "A Source Player Name must be provided!")
            String sourcePlayer,
            @Positive(message = "ItemID must be Positive!")
            Integer itemId
    ) {
    }

    /**
     * ReST
     * Only used for creating a Game Room.
     * @param gameRoomName
     * @param playerAmount
     * @param worldAmount
     * @param gameRoomPassword
     * @param worldType MULTIWORLD || COOP
     */
    public record CreateRoomRecord(

            @NotEmpty(message = "Game Room Name must be Provided!")
            String gameRoomName,
            @Positive(message = "Player Amount must be Positive!")
            Integer playerAmount,
            @Positive(message = "World Amount must be Positive!")
            Integer worldAmount,
            @NotEmpty(message = "All Game Rooms need a Password!")
            String gameRoomPassword,
            @NotNull(message = "WorldType must be Provided!")
            WorldType worldType
    ) {
    }

    /**
     * This model is still not finalized.
     * @param stageId
     * @param mainOffset
     * @param secondaryOffset
     * @param sourcePlayerName
     */
    public record EventRecord(
            @NotNull
            Integer stageId,
            @NotNull
            Integer mainOffset,
            @NotNull
            Integer secondaryOffset,
            @NotNull
            String sourcePlayerName
    ) {
    }

    public record ItemRecord(
            @Positive(message = "World Id must be greater than 0.")
            Integer sourcePlayerWorldId,
            @Positive(message = "World Id must be greater than 0.")
            Integer targetPlayerWorldId,
            @PositiveOrZero(message = "Item Ids cannot be less than 0.")
            Integer itemId
    ) {
    }

    public record foo(Integer bar) {
        public Integer getBar() {
            return this.bar();
        }
    }
}

