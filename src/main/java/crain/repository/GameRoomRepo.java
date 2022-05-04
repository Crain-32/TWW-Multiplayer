package crain.repository;


import crain.model.domain.GameRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameRoomRepo extends JpaRepository<GameRoom, Long> {

    boolean existsByName(String gameRoomName);

    GameRoom findOneByName(String gameRoomName);

    boolean existsByNameAndPassword(String gameRoomName, String password);

    List<GameRoom> findAllByPlayersConnectedFalseOrPlayersIsNull();
}
