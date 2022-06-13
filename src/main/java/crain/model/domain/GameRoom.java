package crain.model.domain;

import crain.exceptions.InvalidPlayerException;
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
public class GameRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String password;
    private Integer worldAmount;
    private Integer connectedPlayerCount;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();

    private Boolean tournament;

    @PreRemove
    public void beforeRemoval() {
        this.players.removeAll(this.players);
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
        boolean playerMatch = this.players.stream()
                .noneMatch(existingPlayer -> existingPlayer.softEqualityWithPlayer(player));
        return playerMatch && this.worldAmount >= player.getWorldId();
    }
}
