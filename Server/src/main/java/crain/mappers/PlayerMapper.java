package crain.mappers;

import crain.model.domain.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import records.DETAIL;
import records.ROOM;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlayerMapper {

    ROOM.PlayerRecord createPlayerRecord(Player player);

    @Mapping(target="lastInteractionDate", source="player.lastInteractionDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    DETAIL.Player detailedPlayer(Player player);


}
