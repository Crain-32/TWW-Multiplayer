package crain.service;

import crain.exceptions.InvalidPlayerException;
import crain.mappers.PlayerMapper;
import crain.model.domain.Player;
import crain.model.event.CoopItemEvent;
import crain.repository.PlayerRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import records.DETAIL;
import records.ROOM;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepo playerRepo;
    private final PlayerMapper playerMapper;

    @SneakyThrows
    public Player setPlayerToConnected(ROOM.PlayerRecord dto, String gameRoomName) {
        Player player = playerRepo.findByPlayerNameIgnoreCaseAndGameRoomName(dto.playerName(), gameRoomName)
                .map(val -> val.makeConnected(true))
                .orElseThrow(() -> new InvalidPlayerException("Player could not be found."));
        return playerRepo.save(player);
    }

    public void setOldPlayersToDisconnected() {
        try {
            List<Player> oldPlayers = playerRepo.findAllByLastInteractionDateBefore(Instant.now().minus(1, ChronoUnit.DAYS));
            oldPlayers.forEach(player -> player.setConnected(false));
            playerRepo.saveAll(oldPlayers);
        } catch (Exception e) {
            log.info("Failed to update old Players", e);
        }
    }

    @SneakyThrows
    public DETAIL.Player getDetailedPlayer(ROOM.PlayerRecord dto, String gameRoomName) {
        return playerRepo.findByPlayerNameIgnoreCaseAndGameRoomName(dto.playerName(), gameRoomName)
                .map(playerMapper::detailedPlayer)
                .orElseThrow(() -> new InvalidPlayerException("Player could not be found."));
    }

    @Async
    @EventListener
    public void handleCoopItem(CoopItemEvent event) {
        var optionalPlayer = playerRepo.findByPlayerNameIgnoreCaseAndGameRoomName(event.itemRecord().sourcePlayer(), event.gameRoom());
        if (optionalPlayer.isPresent()) {
            var player = optionalPlayer.get();
            playerRepo.save(player);
        }
    }
}
