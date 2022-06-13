package crain.service;

import crain.exceptions.InvalidGameRoomException;
import crain.exceptions.InvalidPlayerException;
import crain.model.domain.GameRoom;
import crain.model.domain.Player;
import crain.model.dto.*;
import crain.repository.GameRoomRepo;
import crain.repository.PlayerRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameRoomService {

    private final GameRoomRepo gameRoomRepo;
    private final PlayerService playerService;
    private final PlayerRepo playerRepo;

    public List<GameRoom> findAll() {
        return gameRoomRepo.findAll();
    }

    @SneakyThrows
    public void createGameRoom(CreateRoomDto dto) {
        if (gameRoomRepo.existsByName(dto.getGameRoomName())) {
            throw new InvalidGameRoomException("A Gameroom with this Name already Exists!");
        }
        GameRoom gameRoom = GameRoom.builder()
                            .name(dto.getGameRoomName())
                            .password(dto.getGameRoomPassword())
                            .worldAmount(dto.getWorldAmount())
                            .players(new ArrayList<>(dto.getPlayerAmount()))
                            .build();
        gameRoomRepo.saveAndFlush(gameRoom);
    }

    public boolean validateGameRoomPassword(@NonNull String gameRoomName, @NonNull String password) {
        return gameRoomRepo.existsByNameAndPassword(gameRoomName, password);
    }

    public PlayerDto getPlayerStatus(PlayerDto playerDto, String gameRoomName) {
        return PlayerDto.fromEntity(
                playerRepo.findByPlayerNameIgnoreCaseAndGameRoomName(
                        playerDto.getPlayerName(),
                        gameRoomName)
        );
    }

    public void addPlayerDto(PlayerDto playerDto, String gameRoomName) {
        GameRoom gameRoom = gameRoomRepo.findOneByName(gameRoomName);
        Player player = Player.fromDto(playerDto);
        player.setConnected(false);
        // The InvalidPlayerException from below is caught by the Global Exception Handler
        gameRoom.addPlayer(player);
        playerRepo.save(player);
        gameRoomRepo.save(gameRoom);
    }

    @SneakyThrows
    public void setPlayerToConnected(@NonNull PlayerDto dto, @NonNull String gameRoomName, @NonNull String password) {
        if (validateGameRoomPassword(gameRoomName, password)) {
            GameRoom gameRoom = gameRoomRepo.findOneByName(gameRoomName);
            playerService.setPlayerToConnected(dto, gameRoomName);
        } else {
            throw new InvalidGameRoomException("Could not find GameRoom", gameRoomName);
        }
    }


    @SneakyThrows
    public void givePlayerInGameRoomItem(@NonNull String gameRoomName, @NonNull ItemDto itemDto) {
        Player targetPlayer = playerRepo.findByWorldIdAndGameRoomName(itemDto.getTargetPlayerWorldId(), gameRoomName);
        if (Objects.nonNull(targetPlayer)) {
            targetPlayer.getItems().add(itemDto.getItemId());
            playerRepo.save(targetPlayer);
        } else {
            throw new InvalidPlayerException("Could not find Player!", gameRoomName);
        }
    }

    @Transactional
    public void clearEmptyGameRooms() {
        List<GameRoom> gameRooms = gameRoomRepo.findAllDistinctByPlayersConnectedFalseOrPlayersIsNull();
        gameRoomRepo.flush();
        gameRoomRepo.deleteAll(gameRooms);
        gameRoomRepo.flush();
        System.out.println("It worked!");
    }

    public List<GameRoomDto> getEmptyGameRooms() {
        List<GameRoom> gameRooms = gameRoomRepo.findAllDistinctByPlayersConnectedFalseOrPlayersIsNull();
        return gameRooms.stream().map(GameRoomDto::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteGameRoomByName(@NonNull String gameRoomName) {
        GameRoom targetRoom = gameRoomRepo.findOneByName(gameRoomName);
        if (Objects.nonNull(targetRoom)) {
            gameRoomRepo.delete(targetRoom);
        }
        return gameRoomRepo.existsByName(gameRoomName);
    }

    @Transactional
    @SneakyThrows
    public boolean setTournamentMode(@NonNull String gameRoomName, @NonNull boolean setTo) {
        GameRoom targetRoom = gameRoomRepo.findOneByName(gameRoomName);
        if (Objects.nonNull(targetRoom)) {
            targetRoom.setTournament(setTo);
            gameRoomRepo.saveAndFlush(targetRoom);
        } else {
            throw new InvalidGameRoomException("The Provided Gameroom does not Exist!", gameRoomName);
        }
        return targetRoom.getTournament();
    }

    @SneakyThrows
    public TournamentDto getTournamentDto(@NonNull String gameRoomName) {
        GameRoom targetRoom = gameRoomRepo.findOneByName(gameRoomName);
        if (Objects.nonNull(targetRoom)) {
            return TournamentDto.fromGameRoom(targetRoom);
        }
        throw new InvalidGameRoomException("The Provided GameRoom does not Exist!", gameRoomName);
    }
}
