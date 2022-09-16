package crain.repository;

import crain.model.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PlayerRepo extends JpaRepository<Player, Long> {

    Optional<Player> findByPlayerNameIgnoreCaseAndGameRoomName(String playerName, String gameRoomName);

    Optional<Player> findByWorldIdAndGameRoomName(Integer worldId, String gameRoomName);

    List<Player> findAllByLastInteractionDateBefore(Date inputDate);

    Integer countAllByConnectedTrue();
}
