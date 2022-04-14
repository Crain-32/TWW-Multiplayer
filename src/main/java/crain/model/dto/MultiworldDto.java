package crain.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MultiworldDto {

    private int sourcePlayerWorldId;
    private int targetPlayerWorldId;
    private int itemId;

    public MultiworldDto(int sourcePlayerId, int targetPlayerId, int itemId) {
        this.sourcePlayerWorldId = sourcePlayerId;
        this.targetPlayerWorldId = targetPlayerId;
        this.itemId = itemId;
    }
}
