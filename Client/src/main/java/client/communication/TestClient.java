package client.communication;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import records.ROOM;

import java.util.List;

@FeignClient(value = "testClient", url = "${multiplayer.server.url}:${multiplayer.server.port}")
public interface TestClient {

    @GetMapping("/admin/gameroom")
    @Async
    List<ROOM.GameRoomRecord> getTest();

}
