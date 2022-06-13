package crain.controller;

import crain.model.dto.GameRoomDto;
import crain.repository.PlayerRepo;
import crain.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/admin")
@ConditionalOnProperty(name="enable.admin.controller", havingValue="true")
public class AdminController {

    private final GameRoomService gameRoomService;
    private final PlayerRepo playerRepo;

    @GetMapping("/gameroom")
    public List<GameRoomDto> getAllGameRooms() {
        return gameRoomService.findAll().stream()
                .map(GameRoomDto::fromEntity).collect(Collectors.toList());
    }

    @DeleteMapping("/{GameRoom}")
    public boolean deleteGameRoom(@PathVariable("GameRoom") String gameRoomName) {
        return gameRoomService.deleteGameRoomByName(gameRoomName);
    }

    @PutMapping("/{GameRoom}")
    public boolean toggleTournamentMode(@PathVariable("GameRoom") String gameRoomName, @PathParam("setTo") boolean setTo) {
        return gameRoomService.setTournamentMode(gameRoomName, setTo);
    }

    @GetMapping("/total/players")
    public int getTotalConnectedPlayers() {
        return playerRepo.countAllByConnectedTrue();
    }


    @GetMapping("/gameroom/empty")
    public List<GameRoomDto> getAllEmpty() {
        return gameRoomService.getEmptyGameRooms();
    }

}
