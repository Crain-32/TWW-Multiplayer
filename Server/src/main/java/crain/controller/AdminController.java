package crain.controller;

import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{GameRoom}")
    public void deleteGameRoom(@PathVariable("GameRoom") String gameRoomName) {
        applicationEventPublisher.publishEvent(new GameRoomMessageEvent("This Room is being cleared by Crain", gameRoomName));
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

    public boolean eh(Authentication auth, Object ref) {
        return false;
    }

    @PostMapping("/gameroom/message/all")
    public void tellAllGameRooms(@RequestBody String message) {
        getAllGameRooms().forEach(room ->
                applicationEventPublisher.publishEvent(new GameRoomMessageEvent(message, room.name()))
        );

    }


    @SneakyThrows
    @PostMapping("/force/{event}/{GameRoom}")
    public void forceEvent(@PathVariable("GameRoom") String gameRoomName, @PathVariable("event") EventTypes type,
                           @RequestBody String eventInfo) {
        MultiplayerEvent<?> event = switch (type) {
            case COOP ->
                    new CoopItemEvent(gameRoomName, objectMapper.readValue(eventInfo, INFO.CoopItemRecord.class));
            case MULTI ->
                    new MultiworldItemEvent(gameRoomName, objectMapper.readValue(eventInfo, INFO.ItemRecord.class));
            case NAME ->
                    new NameEvent(gameRoomName, objectMapper.readValue(eventInfo, ROOM.PlayerRecord.class));
            case EVENT ->
                    new EventEvent(gameRoomName, objectMapper.readValue(eventInfo, INFO.EventRecord.class));
            case GENERAL -> new GameRoomMessageEvent(gameRoomName, eventInfo);
            case ERROR -> new ErrorEvent(gameRoomName, eventInfo);
        };
        applicationEventPublisher.publishEvent(event);
    }
}
