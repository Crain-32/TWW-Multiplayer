package crain.mappers;

import crain.model.domain.Player;
import crain.model.records.ROOM;
import org.mapstruct.Mapper;

@Mapper
public interface PlayerMapper {

    ROOM.PlayerRecord createPlayerRecord(Player player);
}
