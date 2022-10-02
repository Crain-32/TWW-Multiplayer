package crain.mappers;

import crain.model.domain.GameRoom;
import crain.model.records.DETAIL;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import records.ROOM;

@Mapper(uses={PlayerMapper.class}, componentModel = MappingConstants.ComponentModel.SPRING)
public interface GameRoomMapper {
    @Mapping(target="extraValidation", expression="java(gameRoom.getPassword() != null)")
    @Mapping(target="playerAmount", expression="java(gameRoom.getConnectedPlayerCount())")
    ROOM.GameRoomRecord gameRoomRecordMapper(GameRoom gameRoom);

    @Mapping(target="extraValidation", expression="java(gameRoom.getPassword() != null)")
    @Mapping(target="creationTimestamp", source="gameRoom.creationTimestamp", dateFormat = "dd-MM-yyyy HH:mm:ss")
    DETAIL.GameRoom detailedRoomMapper(GameRoom gameRoom);
}
