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
import records.DETAIL;
import records.INFO;
import records.ROOM;

import java.util.List;

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
        return gameRoomService.findAll().stream().map(gameRoomMapper::gameRoomRecordMapper).toList();
    }

    @DeleteMapping("/{GameRoom}")
    public String deleteGameRoom(@PathVariable("GameRoom") String gameRoomName) {
        applicationEventPublisher.publishEvent(new GameRoomMessageEvent("This Room is being cleared by Crain.", gameRoomName));
        return gameRoomService.deleteGameRoomByName(gameRoomName) ? "GameRoom Still Exists" : "Successfully Deleted";
    }

    @PutMapping("/{GameRoom}") // ?setTo=False
    public Boolean toggleTournamentMode(@PathVariable("GameRoom") String gameRoomName, @RequestParam("setTo") Boolean setTo) {
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

    @GetMapping("/gameroom/detailed")
    public List<DETAIL.GameRoom> getAllDetailedRooms() {
        return gameRoomService.findAll().stream().map(gameRoomMapper::detailedRoomMapper).toList();
    }


    @PostMapping("/gameroom/message/all")
    public void tellAllGameRooms(@RequestBody String message) {
        var allGameRooms = getAllGameRooms();
        for (var room : allGameRooms) {
            applicationEventPublisher.publishEvent(new GameRoomMessageEvent(message, room.name()));
        }
    }


    @SneakyThrows
    @PostMapping("/force/{event}/{GameRoom}")
    public void forceEvent(@PathVariable("GameRoom") String gameRoomName, @PathVariable("event") EventTypes type,
                           @RequestBody String eventInfo) {
        switch (type) {
            case COOP ->
                    applicationEventPublisher.publishEvent(new CoopItemEvent(objectMapper.readValue(eventInfo, INFO.CoopItemRecord.class), gameRoomName));
            case MULTI ->
                    applicationEventPublisher.publishEvent(new MultiworldItemEvent(objectMapper.readValue(eventInfo, INFO.ItemRecord.class), gameRoomName));
            case NAME ->
                    applicationEventPublisher.publishEvent(new NameEvent(objectMapper.readValue(eventInfo, ROOM.PlayerRecord.class), gameRoomName));
            case EVENT ->
                    applicationEventPublisher.publishEvent(new EventEvent(objectMapper.readValue(eventInfo, INFO.EventRecord.class), gameRoomName));
            case GENERAL -> applicationEventPublisher.publishEvent(new GameRoomMessageEvent(eventInfo, gameRoomName));
            case ERROR -> applicationEventPublisher.publishEvent(new ErrorEvent(eventInfo, gameRoomName));
        }
    }
}
