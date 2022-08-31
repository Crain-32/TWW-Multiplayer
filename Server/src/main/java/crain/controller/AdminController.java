package crain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import crain.mappers.GameRoomMapper;
import crain.model.enums.EventTypes;
import crain.model.event.*;
import crain.repository.PlayerRepo;
import crain.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import records.INFO;
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
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

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


    @SneakyThrows
    @PostMapping("/force/event")
    public void forceEvent(@PathVariable("GameRoom") String gameRoomName, @PathVariable("event") EventTypes type,
                           @RequestBody String eventInfo) {
        switch(type) {
            case COOP -> applicationEventPublisher.publishEvent(new CoopItemEvent(objectMapper.readValue(eventInfo, INFO.CoopItemRecord.class), gameRoomName));
            case MULTI -> applicationEventPublisher.publishEvent(new MultiworldItemEvent(objectMapper.readValue(eventInfo, INFO.ItemRecord.class), gameRoomName));
            case NAME -> applicationEventPublisher.publishEvent(new NameEvent(objectMapper.readValue(eventInfo, ROOM.PlayerRecord.class), gameRoomName));
            case EVENT -> applicationEventPublisher.publishEvent(new EventEvent(objectMapper.readValue(eventInfo, INFO.EventRecord.class), gameRoomName));
            case GENERAL -> applicationEventPublisher.publishEvent(new GeneralMessageEvent(eventInfo, gameRoomName));
            case ERROR -> applicationEventPublisher.publishEvent(new ErrorEvent(eventInfo, gameRoomName));
        }
    }
}
