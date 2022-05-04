package crain.model.dto;

import crain.model.domain.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentPlayerDto {

    private String name;
    private Integer worldId;
    private List<Integer> items;

    public static TournamentPlayerDto fromPlayer(Player player) {
        return TournamentPlayerDto.builder()
                .name(player.getPlayerName())
                .worldId(player.getWorldId())
                .items(player.getItems())
                .build();
    }
}
