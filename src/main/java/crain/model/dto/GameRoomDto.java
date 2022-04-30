package crain.model.dto;

import crain.model.domain.GameRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameRoomDto implements Serializable {

    private Long id;
    private String name;
    private Boolean extraValidation;
    private Integer worldAmount;
    private Integer playerAmount;
    private List<PlayerDto> players;


    public static GameRoomDto fromEntity(GameRoom gameRoom) {
        List<PlayerDto> playerDtoList = gameRoom.getPlayers().stream()
                                        .map(PlayerDto::fromEntity)
                                        .collect(Collectors.toList());
        return GameRoomDto.builder()
                .id(gameRoom.getId())
                .name(gameRoom.getName())
                .extraValidation(Objects.nonNull(gameRoom.getPassword()))
                .worldAmount(gameRoom.getWorldAmount())
                .playerAmount(playerDtoList.size())
                .players(playerDtoList)
                .build();
    }
}
