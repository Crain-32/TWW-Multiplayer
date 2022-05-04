package crain.model.domain;

import crain.exceptions.InvalidPlayerException;
import crain.model.dto.PlayerDto;
import lombok.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class GameRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String password;
    private Integer worldAmount;
    private Integer connectedPlayerCount;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Player> players;

    private Boolean tournament;

    @SneakyThrows
    @Transactional
    public void addPlayer(Player player) {
        boolean playerMatch = this.players.stream().allMatch(existingPlayer -> existingPlayer.validateOtherPlayer(player));
        if (playerMatch && this.worldAmount > player.getWorldId()) {
            this.players.add(player);
            player.setGameRoom(this);
        } else {
            throw new InvalidPlayerException("The Provided Player Cannot be Added!", this.name);
        }
    }
}
