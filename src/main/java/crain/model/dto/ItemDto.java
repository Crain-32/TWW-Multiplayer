package crain.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
public class ItemDto {

    @Positive(message="World Id must be greater than 0.")
    private int sourcePlayerWorldId;
    @Positive(message="World Id must be greater than 0.")
    private int targetPlayerWorldId;
    @PositiveOrZero(message="Item Ids cannot be less than 0.")
    private int itemId;

    public ItemDto(int sourcePlayerId, int targetPlayerId, int itemId) {
        this.sourcePlayerWorldId = sourcePlayerId;
        this.targetPlayerWorldId = targetPlayerId;
        this.itemId = itemId;
    }
}
