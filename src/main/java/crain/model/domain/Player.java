package crain.model.domain;

import crain.exceptions.InvalidPlayerException;
import crain.model.constants.WorldType;
import crain.model.dto.PlayerDto;
import lombok.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String playerName;

    @ManyToOne
    @JoinColumn(name = "gameroom_id", nullable = false)
    private GameRoom gameRoom;

    @Enumerated
    private WorldType worldType;
    private Integer worldId;
    private Boolean connected;

    @ElementCollection
    private List<Integer> items = new ArrayList<>();

    public static Player fromDto(PlayerDto dto) {
        return Player.builder()
                .playerName(dto.getPlayerName())
                .worldType(WorldType.valueOf(dto.getWorldType().toUpperCase()))
                .worldId(dto.getWorldId())
                .build();
    }

    /**
     * Validates if the other Player object would be a valid Player in the same GameRoom.
     * Matching Criteria (Returns False if any of the following are True)
     * - Same Name (LowerCased)
     * - Same World ID - If Both WorldType != WorldType.SHARED
     *
     * @param player Player to Compare Against
     * @return boolean
     */
    public boolean validateOtherPlayer(@NonNull Player player) {
        // Yes, this looks terrible. I do not care.
        // The only change would be swapping the elements in the First && for Performance
        return (this.playerName.equalsIgnoreCase(player.getPlayerName()) &&
                (!this.worldId.equals(player.worldId) ||
                    (this.worldType.equals(WorldType.SHARED) &&
                    player.worldType.equals(WorldType.SHARED))));
    }

}
