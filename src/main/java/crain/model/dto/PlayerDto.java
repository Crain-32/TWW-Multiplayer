package crain.model.dto;

import crain.model.domain.Player;
import lombok.Data;

@Data
public class PlayerDto {

    private int worldId = -1; // Mirrors Player Id
    private String userName;
    private boolean connected;


    public static PlayerDto fromEntity(Player player) {
        PlayerDto playerDto = new PlayerDto();
        playerDto.setWorldId(player.getWorldId());
        playerDto.setUserName(player.getUserName());
        playerDto.setConnected(player.isConnected());
        return playerDto;
    }
}
