package crain.model.dto;

import crain.model.domain.GameRoom;
import lombok.Data;
import lombok.NonNull;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GameRoomDto {

    private String gameRoomName;
    private Boolean extraValidation;
    private List<PlayerDto> playerDto;

    public static GameRoomDto fromEntity(@NonNull GameRoom gameRoom) {
        GameRoomDto gameRoomDto = new GameRoomDto();
        gameRoomDto.setGameRoomName(gameRoom.getGameRoomName());
        gameRoomDto.setExtraValidation(StringUtils.hasText(gameRoom.getGameRoomPassword()));
        gameRoomDto.setPlayerDto(
                gameRoom.getPlayers().stream().map(PlayerDto::fromEntity).collect(Collectors.toList())
        );
        return gameRoomDto;
    }
}
