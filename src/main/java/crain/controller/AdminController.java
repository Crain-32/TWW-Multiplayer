package crain.controller;

import crain.model.dto.GameRoomDto;
import crain.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController("/rest/admin")
@ConditionalOnProperty(prefix="", name="enable.admin.controller", havingValue="true")
public class AdminController {

    private final GameRoomService gameRoomService;

    @GetMapping("/gameroom")
    public List<GameRoomDto> getAllGameRooms() {
        return gameRoomService.findAll().stream()
                .map(GameRoomDto::fromEntity).collect(Collectors.toList());
    }

    @DeleteMapping("/{gameroom}")
    public boolean deleteGameRoom(@PathVariable("gameroom") String gameRoomName) {
        return gameRoomService.deleteGameRoomByName(gameRoomName);
    }

    @PutMapping("/{gameroom}")
    public boolean toggleTournamentMode(@PathVariable("gameroom") String gameRoomName, @PathParam("setTo") boolean setTo) {
        return gameRoomService.setTournamentMode(gameRoomName, setTo);
    }
}
