package crain.client.communication;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import records.DETAIL;
import records.INFO;
import records.ROOM;

@FeignClient(value = "gameRoomClient", url = "http://${multiplayer.server.url}:${multiplayer.server.port}", path = "/rest/gameroom")
public interface GameRoomApi {


    @PostMapping
    void createGameRoom(@RequestBody INFO.CreateRoomRecord record);


    @PostMapping("/{GameRoom}")
    Boolean addPlayerToGameRoom(@PathVariable("GameRoom") String gameRoomName,
                                @RequestParam("password") String password,
                                @RequestBody ROOM.PlayerRecord playerRecord);

    @PostMapping("/{GameRoom}/player")
    ROOM.PlayerRecord getPlayerStatus(@PathVariable("GameRoom") String gameRoomName,
                                      @RequestParam("password") String password,
                                      @RequestBody ROOM.PlayerRecord playerRecord);

    @GetMapping("/{GameRoom}/player/detail")
    DETAIL.Player getDetailedPlayer(@PathVariable("GameRoom") String gameRoomName,
                            @RequestParam("password") String password,
                            @RequestBody ROOM.PlayerRecord playerRecord);
}
