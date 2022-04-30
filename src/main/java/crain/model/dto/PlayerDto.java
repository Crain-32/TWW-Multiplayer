package crain.model.dto;

import crain.model.domain.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto implements Serializable {

    private Long id;
    private String playerName;
    private Integer worldId;
    private String worldType;
    private Boolean connected;

    public static PlayerDto fromEntity(Player player) {
        String worldType = "";
        switch (player.getWorldType()) {
            case COOP:
                worldType = "COOP";
                break;
            case SHARED:
                worldType = "SHARED";
                break;
            case MULTIWORLD:
                worldType = "MULTIWORLD";
                break;
        }
        return PlayerDto.builder()
                .id(player.getId())
                .playerName(player.getPlayerName())
                .worldId(player.getWorldId())
                .worldType(worldType)
                .connected(player.getConnected())
                .build();
    }
}
