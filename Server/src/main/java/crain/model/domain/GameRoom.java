package crain.model.domain;

import constants.WorldType;
import crain.exceptions.InvalidPlayerException;
import lombok.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String password;
    private Integer worldAmount;
    private Integer connectedPlayerCount;

    @Enumerated
    private WorldType worldType;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();
    private Boolean tournament;

    private Date creationTimestamp;


    @PrePersist
    public void beforePersist() {
        this.creationTimestamp = Date.from(Instant.now());
    }

    @PreRemove
    public void beforeRemoval() {
        players.clear();
    }


    @SneakyThrows
    @Transactional
    public void addPlayer(Player player) {
        if (this.canAddPlayer(player)) {
            this.players.add(player);
            player.setGameRoom(this);
        } else {
            throw new InvalidPlayerException("The Provided Player Cannot be Added!", this.name);
        }
    }

    @Transactional
    public boolean canAddPlayer(Player player) {
        if (this.worldType == WorldType.COOP && this.worldType == player.getWorldType()) {
            return true;
        }
        boolean playerMatch = this.players.stream()
                .noneMatch(existingPlayer -> existingPlayer.softEqualityWithPlayer(player));
        return playerMatch && this.worldAmount >= player.getWorldId();
    }
}
