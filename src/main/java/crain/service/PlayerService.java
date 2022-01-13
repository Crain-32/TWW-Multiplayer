package crain.service;

import crain.model.domain.GameRoom;
import crain.model.domain.Item;
import crain.model.domain.Player;
import crain.model.dto.ItemDto;
import crain.repository.GameRoomRepository;
import crain.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final GameRoomRepository gameRoomRepository;

    @SneakyThrows
    public boolean connectPlayer(String gameRoomName, int playerId) {
        Player player = gameRoomRepository.getPlayerInGameRoomFromNameAndId(gameRoomName, playerId);
        if (!player.isConnected()) {
            player.setConnected(true);
            playerRepository.save(player);
            return true;
        }
        return false;
    }

    @SneakyThrows
    public void addItemToQueue(GameRoom gameRoom, ItemDto dto) {
        Player targetPlayer = gameRoom.getByWorldId(dto.getTargetPlayerWorldId());
        Player sourcePlayer = gameRoom.getByWorldId(dto.getSourcePlayerWorldId());
        if (targetPlayer == null) {
            throw new IllegalAccessException("Target Player doesn't exist");
        }
        if (sourcePlayer == null) {
            throw new IllegalAccessException("Source Player doesn't exist");
        }
        Item item = new Item(sourcePlayer.getWorldId(), dto.getItemId());
        targetPlayer.getQueuedItems().add(item);
        playerRepository.save(targetPlayer);
    }
}
