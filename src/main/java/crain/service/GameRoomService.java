package crain.service;

import crain.exceptions.InvalidGameRoomException;
import crain.model.domain.GameRoom;
import crain.model.domain.Player;
import crain.model.dto.CreateRoomDto;
import crain.repository.GameRoomRepo;
import crain.repository.PlayerRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GameRoomService {

    private final GameRoomRepo gameRoomRepo;
    private final PlayerRepo playerRepo;

    public List<GameRoom> findAll() {
        return gameRoomRepo.findAll();
    }

    public void createGameRoom(CreateRoomDto dto) throws InvalidGameRoomException {
        if (gameRoomRepo.existsByName(dto.getGameRoomName())) {
            throw new InvalidGameRoomException("Gameroom with this Name Already Exists!");
        }
        GameRoom gameRoom = GameRoom.builder()
                            .name(dto.getGameRoomName())
                            .password(dto.getGameRoomPassword())
                            .worldAmount(dto.getWorldAmount())
                            .players(new ArrayList<>(dto.getPlayerAmount()))
                            .build();
        gameRoomRepo.saveAndFlush(gameRoom);
    }

    public String getGameRoomPassword(@NonNull String gameRoomName) {
        GameRoom gameRoom = gameRoomRepo.findOneByName(gameRoomName);
        if (Objects.isNull(gameRoom)) {
            return null;
        }
        return gameRoom.getPassword();
    }
}
