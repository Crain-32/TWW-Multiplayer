package crain.repository;

import crain.model.domain.Player;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepo extends JpaRepository<Player, Long> {

    Option<Player> findByPlayerNameIgnoreCaseAndGameRoomName(String playerName, String gameRoomName);

    Option<Player> findByWorldIdAndGameRoomName(Integer worldId, String gameRoomName);

    Integer countAllByConnectedTrue();
}
