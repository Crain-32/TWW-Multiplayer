package crain.controller;

import crain.exceptions.InvalidGameRoomException;
import crain.exceptions.InvalidPlayerException;
import crain.model.dto.CreateRoomDto;
import crain.model.dto.PlayerDto;
import crain.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/gameroom")
public class GameRoomController {
    private final GameRoomService gameRoomService;


    @PostMapping
    @SneakyThrows
    public void createGameRoom(@Validated @RequestBody CreateRoomDto dto) {
        gameRoomService.createGameRoom(dto);
    }

    @PostMapping("/{GameRoom}")
    public boolean addPlayerToGameRoom(@Validated @RequestBody PlayerDto dto,
                                       @PathVariable(value = "GameRoom") String gameRoomName,
                                       @RequestParam(value = "password") String password) {
        if (gameRoomService.validateGameRoomPassword(gameRoomName, password)) {
            gameRoomService.addPlayerDto(dto, gameRoomName);
            return true;
        }
        return false;
    }

    @SneakyThrows
    @PostMapping("/{GameRoom}/player")
    public PlayerDto getPlayerStatus(@Validated @RequestBody PlayerDto dto,
                                          @PathVariable(value = "GameRoom") String gameRoomName,
                                          @RequestParam(value = "password") String password) {
        if (gameRoomService.validateGameRoomPassword(gameRoomName, password)) {
            return gameRoomService.getPlayerStatus(dto, gameRoomName);
        }
        throw new InvalidGameRoomException("The Requested Game Room could not be found, or the Password was wrong.", gameRoomName);
    }

    @ExceptionHandler(value= {InvalidGameRoomException.class} )
    public ResponseEntity<String> handleInvalidGameRoom(InvalidGameRoomException e) {
        return new ResponseEntity<>(e.getMessage() + " for " + e.getGameRoomName(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InvalidPlayerException.class})
    public ResponseEntity<String> handleInvalidPlayer(InvalidPlayerException e) {
        return new ResponseEntity<>(e.getMessage() + " for " + e.getGameRoomName(), HttpStatus.NOT_FOUND);
    }
}
