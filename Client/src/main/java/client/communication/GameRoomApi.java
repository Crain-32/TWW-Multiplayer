package client.communication;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import records.INFO;
import records.ROOM;

@FeignClient(value = "gameRoomClient", url = "${multiplayer.server.url}:${multiplayer.server.port}", path = "/rest/gameroom")
public interface GameRoomApi {

    @Async
    @PostMapping
    ResponseEntity<?> createGameRoom(@RequestBody INFO.CreateRoomRecord record);

    @Async
    @PostMapping("/{GameRoom}")
    Boolean addPlayerToGameRoom(@PathVariable("GameRoom") String gameRoomName,
                                @RequestParam("password") String password,
                                @RequestBody ROOM.PlayerRecord playerRecord);

    @Async
    @PostMapping("/{GameRoom}/player")
    ROOM.PlayerRecord getPlayerStatus(@PathVariable("GameRoom") String gameRoomName,
                                      @RequestParam("password") String password,
                                      @RequestBody ROOM.PlayerRecord playerRecord);
}
