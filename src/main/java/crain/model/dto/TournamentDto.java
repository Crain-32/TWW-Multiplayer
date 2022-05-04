package crain.model.dto;

import crain.model.domain.GameRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentDto {

    private String gameRoom;
    private List<TournamentPlayerDto> players;

    public static TournamentDto fromGameRoom(GameRoom sourceGameRoom) {
        return TournamentDto.builder()
                .gameRoom(sourceGameRoom.getName())
                .players(sourceGameRoom.getPlayers().stream()
                        .map(TournamentPlayerDto::fromPlayer)
                        .collect(Collectors.toList()))
                .build();
    }
}
