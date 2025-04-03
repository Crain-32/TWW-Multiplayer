package crain.model.domain;

import constants.WorldType;
import crain.exceptions.InvalidPlayerException;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameRoom extends AbstractPersistable<Long> {

    private String name;
    private String password;
    private Integer worldAmount;
    private Integer connectedPlayerCount;

    @Enumerated
    private WorldType worldType;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();

    private Instant creationTimestamp;

    @PreRemove
    public void beforeRemoval() {
        players.clear();
    }

    public int getConnectedPlayerCount() {
        return Math.toIntExact(this.players.stream().filter(Player::isConnected).count());
    }

    @SneakyThrows
    @Transactional
    public void addPlayer(Player player) {
        if (this.canAddPlayer(player)) {
            this.players.add(player);
            player.setGameRoom(this);
        } else {
            throw new InvalidPlayerException("The provided Player cannot be added", this.name);
        }
    }

    //TODO: Add Max Player Handling for Coop in Here.
    public boolean canAddPlayer(Player player) {
        if (this.worldType == WorldType.COOP && this.worldType == player.getWorldType()) {
            return this.players.stream().noneMatch(existingPlayer -> StringUtils.equalsIgnoreCase(existingPlayer.getPlayerName(), player.getPlayerName()));
        }
        boolean playerMatch = this.players.stream()
                .noneMatch(existingPlayer -> existingPlayer.softEqualityWithPlayer(player));
        return playerMatch && this.worldAmount >= player.getWorldId();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return this.password;
    }

    public Integer getWorldAmount() {
        return this.worldAmount;
    }

    public WorldType getWorldType() {
        return this.worldType;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public Instant getCreationTimestamp() {
        return this.creationTimestamp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setWorldAmount(Integer worldAmount) {
        this.worldAmount = worldAmount;
    }

    public void setConnectedPlayerCount(Integer connectedPlayerCount) {
        this.connectedPlayerCount = connectedPlayerCount;
    }

    public void setWorldType(WorldType worldType) {
        this.worldType = worldType;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @PrePersist
    void setCreationTimestamp() {
        this.creationTimestamp = Instant.now();
    }
}
