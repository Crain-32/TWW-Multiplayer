package crain.mappers;

import crain.model.domain.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import records.DETAIL;
import records.ROOM;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlayerMapper {

    ROOM.PlayerRecord createPlayerRecord(Player player);

    @Mapping(target = "id", source = "worldId")
    @Mapping(target = "queuedItems", source = "items")
    @Mapping(target = "lastInteractionDate", source = "lastInteractionDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    DETAIL.Player detailedPlayer(Player player);


}
