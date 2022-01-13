package crain.repository;

import crain.model.domain.GameRoom;
import crain.model.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRoomRepository extends JpaRepository<GameRoom, Long> {

    GameRoom getGameRoomByGameRoomName(String gameRoomName);

    @Query(value = "SELECT DISTINCT player FROM GameRoom g INNER JOIN g.players player WHERE player.worldId =: playerWorldId AND g.id =: gameRoomId")
    Player getPlayerInGameRoomFromId(@Param("gameRoomId") Long gameRoomId, @Param("playerWorldId") int playerWorldId);

    @Query(value = "SELECT DISTINCT player FROM GameRoom g INNER JOIN g.players player WHERE player.worldId =: playerWorldId AND g.gameRoomName =: gameRoomName")
    Player getPlayerInGameRoomFromNameAndId(@Param("gameRoomName") String gameRoomName, @Param("playerWorldId") int playerWorldId);

    boolean existsGameRoomByGameRoomName(String gameRoomName);
}
