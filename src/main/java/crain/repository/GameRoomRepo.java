package crain.repository;


import crain.model.domain.GameRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRoomRepo extends JpaRepository<GameRoom, Long> {

    boolean existsByName(String gameRoomName);

    GameRoom findOneByName(String gameRoomName);
}
