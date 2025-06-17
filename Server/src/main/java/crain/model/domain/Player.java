package crain.model.domain;

import constants.WorldType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import org.springframework.data.jpa.domain.AbstractPersistable;
import records.ROOM;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
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

    public Player(String playerName, @NotNull GameRoom gameRoom, WorldType worldType, Integer worldId, boolean connected, List<Integer> items, Instant lastInteractionDate) {
        this.playerName = playerName;
        this.gameRoom = gameRoom;
        this.worldType = worldType;
        this.worldId = worldId;
        this.connected = connected;
        this.items = items;
        this.lastInteractionDate = lastInteractionDate;
    }

    public Player() {
    }

    public static PlayerBuilder builder() {
        return new PlayerBuilder();
    }

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

    public static class PlayerBuilder {
        private String playerName;
        private @NotNull GameRoom gameRoom;
        private WorldType worldType;
        private Integer worldId;
        private boolean connected;
        private List<Integer> items;
        private Instant lastInteractionDate;

        PlayerBuilder() {
        }

        public PlayerBuilder playerName(String playerName) {
            this.playerName = playerName;
            return this;
        }

        public PlayerBuilder gameRoom(@NotNull GameRoom gameRoom) {
            this.gameRoom = gameRoom;
            return this;
        }

        public PlayerBuilder worldType(WorldType worldType) {
            this.worldType = worldType;
            return this;
        }

        public PlayerBuilder worldId(Integer worldId) {
            this.worldId = worldId;
            return this;
        }

        public PlayerBuilder connected(boolean connected) {
            this.connected = connected;
            return this;
        }

        public PlayerBuilder items(List<Integer> items) {
            this.items = items;
            return this;
        }

        public PlayerBuilder lastInteractionDate(Instant lastInteractionDate) {
            this.lastInteractionDate = lastInteractionDate;
            return this;
        }

        public Player build() {
            Objects.requireNonNull(this.gameRoom);
            return new Player(
                    this.playerName,
                    this.gameRoom,
                    this.worldType,
                    this.worldId,
                    this.connected,
                    Objects.requireNonNullElseGet(this.items, ArrayList::new),
                    this.lastInteractionDate
            );
        }

        public String toString() {
            return "Player.PlayerBuilder(playerName=" + this.playerName + ", gameRoom=" + this.gameRoom + ", worldType=" + this.worldType + ", worldId=" + this.worldId + ", connected=" + this.connected + ", items=" + this.items + ", lastInteractionDate=" + this.lastInteractionDate + ")";
        }
    }
}
