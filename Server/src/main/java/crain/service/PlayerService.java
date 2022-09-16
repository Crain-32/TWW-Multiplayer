package crain.service;

import crain.exceptions.InvalidPlayerException;
import crain.model.domain.Player;
import crain.repository.PlayerRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import records.ROOM;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Slf4j
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

    public void setOldPlayersToDisconnected() {
        try {
            List<Player> oldPlayers = playerRepo.findAllByLastInteractionDateBefore(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)));
            oldPlayers.forEach(player -> player.setConnected(false));
            playerRepo.saveAll(oldPlayers);
        } catch (Exception e) {
            log.info("Failed to update old Players", e);
        }
    }
}
