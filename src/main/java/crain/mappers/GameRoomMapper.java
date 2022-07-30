package crain.mappers;

import crain.model.domain.GameRoom;
import crain.model.records.ROOM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses={PlayerMapper.class})
public interface GameRoomMapper {
    GameRoomMapper INSTANCE = Mappers.getMapper(GameRoomMapper.class);
    @Mapping(target="extraValidation", expression="java(gameRoom.getPassword() != null)")
    ROOM.GameRoomRecord gameRoomRecordMapper(GameRoom gameRoom);
}
