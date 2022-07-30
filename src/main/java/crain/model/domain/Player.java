package crain.model.domain;

import crain.model.constants.WorldType;
import crain.model.records.ROOM;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @JoinColumn(name = "gameroom_id")
    @NotNull
    private GameRoom gameRoom;

    @Enumerated
    private WorldType worldType;
    private Integer worldId;
    private Boolean connected;

    @PreRemove
    public void preRemove() {
        this.gameRoom = null;
    }

    @ElementCollection(fetch=FetchType.EAGER)
    private List<Integer> items = new ArrayList<>();

    public static Player fromDto(ROOM.PlayerRecord dto) {
        return Player.builder()
                .playerName(dto.playerName())
                .worldType(dto.worldType())
                .worldId(dto.worldId())
                .build();
    }

    /**
     * Validates if the other {@link Player} object would be a valid Player in the same {@link GameRoom}
     * Matching Criteria (Returns False if any of the following are True)
     * *Same Name (LowerCased)
     * *Same World ID - If Both WorldType != WorldType.SHARED
     *
     * @param player Player to Compare Against
     * @return boolean
     */
    public boolean softEqualityWithPlayer(@NonNull Player player) {
        return (this.playerName.equalsIgnoreCase(player.getPlayerName()) ||
                (this.worldId.equals(player.worldId) &&
                    (this.worldType.equals(WorldType.SHARED) &&
                    !player.worldType.equals(WorldType.SHARED))));
    }

}
