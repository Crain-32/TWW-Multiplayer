package crain.mappers;

import crain.model.domain.Player;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import records.ROOM;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlayerMapper {

    ROOM.PlayerRecord createPlayerRecord(Player player);
}
