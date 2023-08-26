package crain.service;

import crain.exceptions.InvalidGameRoomException;
import crain.exceptions.InvalidPlayerException;
import crain.mappers.GameRoomMapper;
import crain.mappers.PlayerMapper;
import crain.model.domain.GameRoom;
import crain.model.domain.Player;
import crain.model.records.TOURNAMENT;
import crain.repository.GameRoomRepo;
import crain.repository.PlayerRepo;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import records.INFO;
import records.ROOM;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameRoomService {

    private final GameRoomRepo gameRoomRepo;
    private final PlayerService playerService;
    private final PlayerRepo playerRepo;
    private final GameRoomMapper gameRoomMapper;
    private final PlayerMapper playerMapper;

    public List<GameRoom> findAll() {
        return gameRoomRepo.findAll();
    }

    @SneakyThrows
    public void createGameRoom(INFO.CreateRoomRecord dto) {
        if (gameRoomRepo.existsByName(dto.gameRoomName())) {
            if (log.isDebugEnabled()) {
                log.debug("Attempted Duplicate Game Room Create with name: " + dto.gameRoomName());
            }
            throw new InvalidGameRoomException("A Game Room with this Name already Exists!");
        }
        GameRoom gameRoom = GameRoom.builder()
                .name(dto.gameRoomName())
                .password(dto.gameRoomPassword())
                .worldType(dto.worldType())
                .worldAmount(dto.worldAmount())
                .players(new ArrayList<>(dto.playerAmount()))
                .build();
        gameRoomRepo.saveAndFlush(gameRoom);
    }

    public boolean validateGameRoomPassword(@NonNull String gameRoomName, @NonNull String password) {
        return gameRoomRepo.existsByNameAndPassword(gameRoomName, password);
    }

    public ROOM.PlayerRecord getPlayerStatus(ROOM.PlayerRecord playerRecord, String gameRoomName) {
        return playerMapper.createPlayerRecord(
                playerRepo.findByPlayerNameIgnoreCaseAndGameRoomName(
                        playerRecord.playerName(),
                        gameRoomName).orElse(new Player()));
    }

    @SneakyThrows
    public void addPlayerDto(ROOM.PlayerRecord playerRecord, String gameRoomName) {
        GameRoom gameRoom = gameRoomRepo.findOneByName(gameRoomName)
                .orElseThrow(() -> new InvalidGameRoomException("The Provided Room does not Exist!", gameRoomName));
        Player player = Player.fromDto(playerRecord);
        player.setConnected(false);
        if (log.isDebugEnabled()) {
            log.debug(playerRecord + " : was added to - " + gameRoomName);
        }
        // The InvalidPlayerException from below is caught by the Global Exception Handler
        gameRoom.addPlayer(player);
        playerRepo.save(player);
        gameRoomRepo.save(gameRoom);
    }

    @SneakyThrows
    public ROOM.PlayerRecord setPlayerToConnected(@NonNull ROOM.PlayerRecord dto, @NonNull String gameRoomName) {
        return playerMapper.createPlayerRecord(playerService.setPlayerToConnected(dto, gameRoomName));
    }


    @SneakyThrows
    public void givePlayerInGameRoomItem(@NonNull String gameRoomName, @NonNull INFO.ItemRecord itemRecord) {
        Optional<Player> targetPlayer = playerRepo.findByWorldIdAndGameRoomName(itemRecord.targetPlayerWorldId(), gameRoomName);
        Player player = targetPlayer.orElseThrow(() -> new InvalidPlayerException("Could not find Player!", gameRoomName));
        player.getItems().add(itemRecord.itemId());
        playerRepo.save(player);
    }

    @Transactional
    public void clearEmptyGameRooms() {
        List<GameRoom> gameRooms = gameRoomRepo.findAllDistinctByPlayersConnectedFalseOrPlayersIsNull();
        var filteredRooms = gameRooms.stream().filter(gameRoom -> gameRoom.getCreationTimestamp()
                .before(
                        Date.from(Instant.now().minus(1, ChronoUnit.DAYS)
                        )
                )).toList();
        gameRoomRepo.flush();
        gameRoomRepo.deleteAll(filteredRooms);
        gameRoomRepo.flush();
    }

    public List<ROOM.GameRoomRecord> getEmptyGameRooms() {
        List<GameRoom> gameRooms = gameRoomRepo.findAllDistinctByPlayersConnectedFalseOrPlayersIsNull();
        gameRooms = gameRooms.stream().filter(gameRoom -> gameRoom.getCreationTimestamp()
                .before(
                        Date.from(Instant.now().minus(1, ChronoUnit.DAYS)
                        )
                )).toList();
        return gameRooms.stream().map(this.gameRoomMapper::gameRoomRecordMapper).collect(Collectors.toList());
    }

    @Transactional
    public Boolean deleteGameRoomByName(@NonNull String gameRoomName) {
        GameRoom targetRoom = gameRoomRepo.findOneByName(gameRoomName).orElse(null);
        if (Objects.nonNull(targetRoom)) {
            gameRoomRepo.delete(targetRoom);
        }
        return gameRoomRepo.existsByName(gameRoomName);
    }

    @Transactional
    @SneakyThrows
    public Boolean setTournamentMode(@NonNull String gameRoomName, @NonNull Boolean setTo) {
        GameRoom targetRoom = gameRoomRepo.findOneByName(gameRoomName)
                .orElseThrow(() -> new InvalidGameRoomException("The Provided Gameroom does not Exist!", gameRoomName));
        targetRoom.setTournament(setTo);
        gameRoomRepo.saveAndFlush(targetRoom);
        return targetRoom.getTournament();
    }

    @SneakyThrows
    public TOURNAMENT.TournamentRecord getTournamentDto(@NonNull String gameRoomName) {
        GameRoom targetRoom = gameRoomRepo.findOneByName(gameRoomName)
                .orElseThrow(() -> new InvalidGameRoomException("The Provided GameRoom does not Exist!", gameRoomName));
        return TOURNAMENT.TournamentRecord.fromGameRoom(targetRoom);
    }
}
