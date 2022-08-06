package crain.service;

import crain.exceptions.InvalidPlayerException;
import crain.model.domain.Player;
import crain.repository.PlayerRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import records.ROOM;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepo playerRepo;

    @SneakyThrows
    public Player setPlayerToConnected(ROOM.PlayerRecord dto, String gameRoomName) {
        Player player = playerRepo.findByPlayerNameIgnoreCaseAndGameRoomName(dto.playerName(), gameRoomName)
                .orElseThrow(() -> new InvalidPlayerException("Player could not be found."));
        player.setConnected(true);
        return playerRepo.save(player);
    }
}