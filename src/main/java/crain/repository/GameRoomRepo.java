package crain.repository;


import crain.model.domain.GameRoom;
import io.vavr.control.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface GameRoomRepo extends JpaRepository<GameRoom, Long> {

    Boolean existsByName(String gameRoomName);

    Option<GameRoom> findOneByName(String gameRoomName);

    Boolean existsByNameAndPassword(String gameRoomName, String password);

    List<GameRoom> findAllDistinctByPlayersConnectedFalseOrPlayersIsNull();

    @Query("SELECT gr FROM GameRoom gr WHERE gr.creationTimestamp <= :compareTimestamp")
    List<GameRoom> findAllGameRoomsCreatedBefore(@Param("compareTimestamp") Date timeStamp);
}
