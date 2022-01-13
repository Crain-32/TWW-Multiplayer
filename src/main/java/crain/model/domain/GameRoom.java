package crain.model.domain;

import crain.model.dto.SetUpDto;
import lombok.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "game_room")
public class GameRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gameRoomName;
    private String gameRoomPassword;
    private Long urlHash;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "game_room_player",
            joinColumns = @JoinColumn(name = "game_room_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "player_id", referencedColumnName = "id")
    )
    private List<Player> players = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGameRoomName() {
        return gameRoomName;
    }

    public void setGameRoomName(String gameRoomName) {
        this.gameRoomName = gameRoomName;
    }

    public String getGameRoomPassword() {
        return gameRoomPassword;
    }

    public void setGameRoomPassword(String gameRoomPassword) {
        this.gameRoomPassword = gameRoomPassword;
    }

    public Long getUrlHash() { return urlHash; }

    public void setUrlHash(Long urlHash) { this.urlHash = urlHash; }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }


    public static GameRoom fromSetUpDto(@NonNull SetUpDto setUpDto) {
        GameRoom gameRoom = new GameRoom();
        gameRoom.setGameRoomName(setUpDto.getGameRoomName());
        gameRoom.setGameRoomPassword(setUpDto.getGameRoomPassword());
        gameRoom.setUrlHash(((long) gameRoom.getGameRoomName().hashCode() + gameRoom.getGameRoomPassword().hashCode()));

        List<Player> players = new ArrayList<>();
        for(int index = 0; index < setUpDto.getWorldAmount(); index++) {
            Player player = new Player(index);
            players.add(player);
        }
        gameRoom.setPlayers(players);

        return gameRoom;
    }

    public Player getByWorldId(int worldId) {
        return this.players.get(worldId);
    }
}
