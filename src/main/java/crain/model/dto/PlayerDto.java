package crain.model.dto;

import crain.model.domain.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto implements Serializable {

    @Null(message ="Requests cannot set this")
    private Long id;

    @Size(min=3, max=20, message="Player Names must be between 3 and 20 Characters")
    private String playerName;

    @Positive(message = "World ID cannot be equal to or less than 0")
    private Integer worldId;

    @NotBlank(message="World Type must be included")
    private String worldType;

    @Null(message = "Requests cannot set this")
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
