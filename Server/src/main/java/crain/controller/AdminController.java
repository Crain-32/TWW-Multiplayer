package crain.controller;

import crain.mappers.GameRoomMapper;
import crain.repository.PlayerRepo;
import crain.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;
import records.ROOM;

import javax.websocket.server.PathParam;
import java.util.List;

import static io.vavr.API.For;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@ConditionalOnProperty(name = "enable.admin.controller", havingValue = "true")
public class AdminController {

    private final GameRoomService gameRoomService;
    private final PlayerRepo playerRepo;
    private final GameRoomMapper gameRoomMapper;

    @GetMapping("/gameroom")
    public List<ROOM.GameRoomRecord> getAllGameRooms() {
        return For(gameRoomService.findAll()).yield(this.gameRoomMapper::gameRoomRecordMapper).toJavaList();
    }

    @DeleteMapping("/{GameRoom}")
    public Boolean deleteGameRoom(@PathVariable("GameRoom") String gameRoomName) {
        return gameRoomService.deleteGameRoomByName(gameRoomName);
    }

    @PutMapping("/{GameRoom}")
    public Boolean toggleTournamentMode(@PathVariable("GameRoom") String gameRoomName, @PathParam("setTo") boolean setTo) {
        return gameRoomService.setTournamentMode(gameRoomName, setTo);
    }

    @GetMapping("/total/players")
    public Integer getTotalConnectedPlayers() {
        return playerRepo.countAllByConnectedTrue();
    }


    @GetMapping("/gameroom/empty")
    public List<ROOM.GameRoomRecord> getAllEmpty() {
        return gameRoomService.getEmptyGameRooms();
    }

}
