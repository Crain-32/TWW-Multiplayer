package crain.service;

import crain.model.domain.GameRoom;
import crain.model.domain.Player;
import crain.model.dto.*;
import crain.repository.GameRoomRepository;
import crain.repository.PlayerRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GameRoomService {

    private final GameRoomRepository gameRoomRepository;
    private final PlayerRepository playerRepository;
    private final PlayerService playerService;


    public void setUpGameRoom(@NonNull SetUpDto setUpDto) throws IllegalArgumentException {
        if(gameRoomRepository.existsGameRoomByGameRoomName(setUpDto.getGameRoomName())) {
            throw new IllegalArgumentException("A Game Room of This Name already exists.");
        }
        GameRoom gameRoom = GameRoom.fromSetUpDto(setUpDto);
        gameRoomRepository.save(gameRoom);
    }

    public GameRoomDto getInfo(@NonNull String gameRoomString) {
        GameRoom gameRoom = gameRoomRepository.getGameRoomByGameRoomName(gameRoomString);
        return GameRoomDto.fromEntity(gameRoom);
    }

    public void addPlayer(PlayerDto dto, String gameRoomString) throws IllegalStateException {
        GameRoom gameRoom = gameRoomRepository.getGameRoomByGameRoomName(gameRoomString);
        Player player = new Player(dto);
        Player potentialPlayer = gameRoomRepository.getPlayerInGameRoomFromId(gameRoom.getId(), dto.getWorldId());
        if (potentialPlayer.getUserName() == null || potentialPlayer.getUserName().isEmpty()) {
            potentialPlayer.setUserName(player.getUserName());
            playerRepository.save(potentialPlayer);
        } else {
            throw new IllegalStateException("A Player for that World already Exists.");
        }
    }

    public boolean validate(@NonNull ConnectionRequestDto dto) {
        GameRoom gameRoom = gameRoomRepository.getGameRoomByGameRoomName(dto.getGameRoomName());
        return gameRoom.getGameRoomPassword().contentEquals(dto.getGameRoomPassword());
    }

    public String connectPlayer(@NonNull ConnectionRequestDto dto) {
        if (validate(dto)) {
            GameRoom gameRoom = gameRoomRepository.getGameRoomByGameRoomName(dto.getGameRoomName());
            if (playerService.connectPlayer(gameRoom.getGameRoomName(), dto.getPlayerDto().getWorldId())) {
                return Long.toString(gameRoom.getUrlHash());
            }
        }
        return "";
    }

    public String receiveItem(@NonNull String gameRoomName, @NonNull ItemDto item) {
        GameRoom gameRoom = gameRoomRepository.getGameRoomByGameRoomName(gameRoomName);
        playerService.addItemToQueue(gameRoom, item);
        return Long.toString(gameRoom.getUrlHash());
    }
}
