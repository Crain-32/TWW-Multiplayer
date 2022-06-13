package crain.controller;

import crain.model.dto.TournamentDto;
import crain.service.GameRoomService;
import crain.service.PlayerService;
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
    private final PlayerService playerService;


    @GetMapping("/{GameRoom}")
    public TournamentDto pollGameRoom(@PathVariable("gameroom") String gameRoomName) {
        return gameRoomService.getTournamentDto(gameRoomName);
    }
}
