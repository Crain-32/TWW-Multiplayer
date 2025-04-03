package crain.repository;


import crain.model.domain.GameRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface GameRoomRepo extends JpaRepository<GameRoom, Long> {

    Boolean existsByName(String gameRoomName);

    Optional<GameRoom> findOneByName(String gameRoomName);

    Boolean existsByNameAndPassword(String gameRoomName, String password);

    List<GameRoom> findAllDistinctByPlayersConnectedFalseOrPlayersIsNull();

    @Query("SELECT gr FROM GameRoom gr WHERE gr.creationTimestamp <= :compareTimestamp")
    List<GameRoom> findAllGameRoomsCreatedBefore(@Param("compareTimestamp") Instant timeStamp);
}
