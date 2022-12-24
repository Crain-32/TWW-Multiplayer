package crain.controller;

import crain.model.records.TOURNAMENT;
import crain.service.GameRoomService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/rest/tournament")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "enable.tournaments", havingValue = "true")
public class TournamentController {

    private final GameRoomService gameRoomService;


    @GetMapping("/{GameRoom}")
    public TOURNAMENT.TournamentRecord pollGameRoom(@PathVariable("GameRoom") String gameRoomName) {
        return gameRoomService.getTournamentDto(gameRoomName);
    }
}
