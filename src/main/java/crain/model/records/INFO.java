package crain.model.records;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class INFO {
    public record CoopItemRecord(
            @NotNull(message = "A Source Player Name must be provided!")
            String sourcePlayer,
            @Positive(message = "ItemID must be Positive!")
            Integer itemId
    ) {
    }

    public record CreateRoomRecord(

            @NotEmpty(message = "Game Room Name must be Provided!")
            String gameRoomName,
            @Positive(message = "Player Amount must be Positive!")
            Integer playerAmount,
            @Positive(message = "World Amount must be Positive!")
            Integer worldAmount,
            @NotEmpty(message = "All Game Rooms need a Password!")
            String gameRoomPassword
    ) {
    }

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
}

