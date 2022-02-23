package crain.repository;

import crain.model.domain.GameRoomPlayerName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRoomPlayerNameRepo extends JpaRepository<GameRoomPlayerName, Long> {
}
