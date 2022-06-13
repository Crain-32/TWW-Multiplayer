package crain.repository;

import crain.model.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepo extends JpaRepository<Player, Long> {

    Player findByPlayerNameIgnoreCaseAndGameRoomName(String playerName, String gameRoomName);

    Player findByWorldIdAndGameRoomName(Integer worldId, String gameRoomName);

    int countAllByConnectedTrue();
}
