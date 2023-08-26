package crain.model.domain;

import constants.WorldType;
import jakarta.persistence.*;
import lombok.*;
import records.ROOM;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @ElementCollection(fetch=FetchType.EAGER)
    private List<Integer> items = new ArrayList<>();

    private Date lastInteractionDate;

    @PreRemove
    public void preRemove() {
        this.gameRoom = null;
    }


    @PrePersist
    @PreUpdate
    public void preUpdated() {
        this.lastInteractionDate = Date.from(Instant.now());
    }
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
                (multiworldCheck(this, player)));
    }

    private boolean multiworldCheck(Player one, Player two) {
        boolean sameWorld = Objects.equals(one.getWorldId(), two.getWorldId());
        if (sameWorld) {
            return !(Objects.equals(one.getWorldType(), two.getWorldType()) && Objects.equals(one.getWorldType(), WorldType.SHARED));
        }
        return false;
    }
}
