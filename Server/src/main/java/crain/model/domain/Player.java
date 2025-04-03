package crain.model.domain;

import constants.WorldType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.AbstractPersistable;
import records.ROOM;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player extends AbstractPersistable<Long> {

    private String playerName;

    @ManyToOne
    @JoinColumn(name = "gameroom_id")
    @NotNull
    private GameRoom gameRoom;

    @Enumerated
    private WorldType worldType;
    private Integer worldId;

    private boolean connected;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> items = new ArrayList<>();

    private Instant lastInteractionDate;

    @PreRemove
    public void preRemove() {
        this.gameRoom = null;
    }

    public Player makeConnected(boolean connection) {
        this.connected = connection;
        return this;
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

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setGameRoom(@NotNull GameRoom gameRoom) {
        if (gameRoom != null) this.gameRoom = gameRoom;
    }

    public void setWorldType(WorldType worldType) {
        if (worldType != null) this.worldType = worldType;
    }

    public void setWorldId(@Positive Integer worldId) {
        this.worldId = worldId;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setItems(List<Integer> items) {
        if (items == null) {
            this.items.clear();
        } else {
            this.items = items;
        }
    }

    public void addItem(Integer item) {
        if (item != null) {
            this.items.add(item);
        }
    }

    public void setLastInteractionDate(Instant lastInteractionDate) {
        this.lastInteractionDate = lastInteractionDate;
    }

    @PreUpdate
    @PrePersist
    void updateLastInteractionDate() {
        setLastInteractionDate(Instant.now());
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public @NotNull GameRoom getGameRoom() {
        return this.gameRoom;
    }

    public WorldType getWorldType() {
        return this.worldType;
    }

    public Integer getWorldId() {
        return this.worldId;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public List<Integer> getItems() {
        return this.items;
    }

    public Instant getLastInteractionDate() {
        return this.lastInteractionDate;
    }
}
