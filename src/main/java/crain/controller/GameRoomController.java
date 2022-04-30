package crain.controller;

import crain.model.domain.GameRoom;
import crain.model.dto.CreateRoomDto;
import crain.model.dto.GameRoomDto;
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

    @GetMapping
    public List<GameRoomDto> getAllGameRooms() {
        return gameRoomService.findAll().stream()
                    .map(GameRoomDto::fromEntity).collect(Collectors.toList());
    }
}
