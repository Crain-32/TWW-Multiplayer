package crain.model.dto;

import crain.model.domain.Item;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ItemDto {

    private int sourcePlayerWorldId;
    private int targetPlayerWorldId;
    private int itemId;


    public ItemDto(@NonNull int sourcePlayerId, @NonNull int targetPlayerId, @NonNull int itemId) {
        this.sourcePlayerWorldId = sourcePlayerId;
        this.targetPlayerWorldId = targetPlayerId;
        this.itemId = itemId;
    }

    public ItemDto(@NonNull Item item) {
        this.sourcePlayerWorldId = item.getFromPlayerWorldId();
        this.itemId = item.getItemId();
    }
}
