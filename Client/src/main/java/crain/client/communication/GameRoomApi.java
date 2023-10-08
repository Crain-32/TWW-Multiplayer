package crain.client.communication;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import records.DETAIL;
import records.INFO;
import records.ROOM;

public interface GameRoomApi {


    @PostExchange
    void createGameRoom(@RequestBody INFO.CreateRoomRecord record);


    @PostExchange("/{GameRoom}")
    Boolean addPlayerToGameRoom(@PathVariable("GameRoom") String gameRoomName,
                                @RequestParam("password") String password,
                                @RequestBody ROOM.PlayerRecord playerRecord);

    @PostExchange("/{GameRoom}/player")
    ROOM.PlayerRecord getPlayerStatus(@PathVariable("GameRoom") String gameRoomName,
                                      @RequestParam("password") String password,
                                      @RequestBody ROOM.PlayerRecord playerRecord);

    @GetExchange("/{GameRoom}/player/detail")
    DETAIL.Player getDetailedPlayer(@PathVariable("GameRoom") String gameRoomName,
                                    @RequestParam("password") String password,
                                    @RequestBody ROOM.PlayerRecord playerRecord);
}
