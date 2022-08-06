package crain.repository;

import crain.model.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepo extends JpaRepository<Player, Long> {

    Optional<Player> findByPlayerNameIgnoreCaseAndGameRoomName(String playerName, String gameRoomName);

    Optional<Player> findByWorldIdAndGameRoomName(Integer worldId, String gameRoomName);

    Integer countAllByConnectedTrue();
}
