package crain.service;

import crain.exceptions.InvalidPlayerException;
import crain.model.domain.Player;
import crain.model.dto.PlayerDto;
import crain.repository.PlayerRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepo playerRepo;

    @SneakyThrows
    public void setPlayerToConnected(PlayerDto dto, String gameRoomName) {
        Player player = playerRepo.findByPlayerNameIgnoreCaseAndGameRoomName(dto.getPlayerName(), gameRoomName);
        if (Objects.isNull(player)) {
            throw new InvalidPlayerException("Provided Player could not be Found!");
        }
        player.setConnected(true);
        playerRepo.save(player);
    }
}
