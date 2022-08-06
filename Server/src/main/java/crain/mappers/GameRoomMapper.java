package crain.mappers;

import crain.model.domain.GameRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import records.ROOM;

@Mapper(uses={PlayerMapper.class}, componentModel = MappingConstants.ComponentModel.SPRING)
public interface GameRoomMapper {
    @Mapping(target="extraValidation", expression="java(gameRoom.getPassword() != null)")
    @Mapping(source="connectedPlayerCount", target="playerAmount")
    ROOM.GameRoomRecord gameRoomRecordMapper(GameRoom gameRoom);
}
