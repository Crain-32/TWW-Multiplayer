package crain.controller;

import crain.exceptions.InvalidGameRoomException;
import crain.exceptions.InvalidPlayerException;
import crain.service.GameRoomService;
import crain.service.PlayerService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import records.DETAIL;
import records.INFO;
import records.ROOM;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rest/gameroom")
public class GameRoomController {
    protected static final String GAMEROOM_MSG = "The requested Game Room could not be found, or the Password was wrong";
    private final GameRoomService gameRoomService;
    private final PlayerService playerService;

    public GameRoomController(GameRoomService gameRoomService, PlayerService playerService) {
        this.gameRoomService = gameRoomService;
        this.playerService = playerService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createGameRoom(@Validated @RequestBody INFO.CreateRoomRecord dto) {
        gameRoomService.createGameRoom(dto);
    }

    @PostMapping("/{GameRoom}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addPlayerToGameRoom(@Validated @RequestBody ROOM.PlayerRecord dto,
                                    @PathVariable(value = "GameRoom") String gameRoomName,
                                    @RequestParam(value = "password") String password) {
        if (!gameRoomService.validateGameRoomPassword(gameRoomName, password)) {
            throw new InvalidGameRoomException(GAMEROOM_MSG, gameRoomName);
        }
        gameRoomService.addPlayerDto(dto, gameRoomName);
    }

    @PostMapping("/{GameRoom}/player")
    public ROOM.PlayerRecord getPlayerStatus(@Validated @RequestBody ROOM.PlayerRecord dto,
                                             @PathVariable(value = "GameRoom") String gameRoomName,
                                             @RequestParam(value = "password") String password) {
        if (gameRoomService.validateGameRoomPassword(gameRoomName, password)) {
            return gameRoomService.getPlayerStatus(dto, gameRoomName);
        }
        throw new InvalidGameRoomException(GAMEROOM_MSG, gameRoomName);
    }


    @GetMapping("/{GameRoom}/player/detail")
    public DETAIL.Player getDetailedPlayer(@Validated @RequestBody ROOM.PlayerRecord dto,
                                           @PathVariable(value = "GameRoom") String gameRoomName,
                                           @RequestParam(value = "password") String password) {
        if (gameRoomService.validateGameRoomPassword(gameRoomName, password)) {
            return playerService.getDetailedPlayer(dto, gameRoomName);
        }
        throw new InvalidGameRoomException(GAMEROOM_MSG, gameRoomName);
    }

    @GetMapping("/{GameRoom}/players")
    public List<ROOM.PlayerRecord> getPlayers(@PathVariable(value = "GameRoom") String gameRoomName,
                                              @RequestParam(value = "password") String password) {
        if (gameRoomService.validateGameRoomPassword(gameRoomName, password)) {
            return gameRoomService.getPlayerRecordsForRoom(gameRoomName);
        }
        throw new InvalidGameRoomException(GAMEROOM_MSG, gameRoomName);
    }

    @ExceptionHandler(value = {InvalidGameRoomException.class})
    public ResponseEntity<String> handleInvalidGameRoom(InvalidGameRoomException e) {
        String body = e.getMessage();
        if (StringUtils.isNotEmpty(e.getGameRoomName())) {
            body += " for " + e.getGameRoomName();
        }
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InvalidPlayerException.class})
    public ResponseEntity<String> handleInvalidPlayer(InvalidPlayerException e) {
        return new ResponseEntity<>(e.getMessage() + " for " + e.getGameRoomName(), HttpStatus.NOT_FOUND);
    }
}
