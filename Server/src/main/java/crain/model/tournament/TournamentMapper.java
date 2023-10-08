package crain.model.tournament;

import crain.model.domain.GameRoom;
import crain.model.domain.Player;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TournamentMapper {

    TournamentPlayer fromPlayer(Player player);

    TournamentRoom fromGameRoom(GameRoom sourceGameRoom);
}
