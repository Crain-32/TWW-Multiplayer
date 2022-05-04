package crain.controller;

import crain.model.domain.GameRoom;
import crain.model.dto.CreateRoomDto;
import crain.model.dto.GameRoomDto;
import crain.model.dto.PlayerDto;
import crain.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/gameroom")
public class GameRoomController {
    private final GameRoomService gameRoomService;

    @PostMapping
    public void createGameRoom(@Validated @RequestBody CreateRoomDto dto) {
        gameRoomService.createGameRoom(dto);
    }

    @PostMapping("/{gameRoomName}")
    public boolean addPlayerToGameRoom(@Validated @RequestBody PlayerDto dto,
                                       @PathVariable(value = "gameRoomName") String gameRoomName,
                                       @RequestParam(value = "password") String password) {
        if (gameRoomService.validateGameRoomPassword(gameRoomName, password)) {
            gameRoomService.addPlayerDto(dto, gameRoomName);
            return true;
        }
        return false;
    }

    @GetMapping("/empty")
    public List<GameRoomDto> getAllEmpty() {
        return gameRoomService.getEmptyGameRooms();
    }

    @GetMapping
    public List<GameRoomDto> findAll() {
        return gameRoomService.findAll().stream().map(GameRoomDto::fromEntity).collect(Collectors.toList());
    }
}
