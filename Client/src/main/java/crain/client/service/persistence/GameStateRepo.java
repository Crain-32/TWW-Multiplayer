package crain.client.service.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameStateRepo extends JpaRepository<GameState, Long> {

    GameState findGameStateByGameRoomName(String gameRoomName);
}
