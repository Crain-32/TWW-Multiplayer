package crain.client.communication.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "multiplayerTrackerApi", url = "${coop.tracker.host}")
public interface MultiplayerTrackerApi {


    @PutMapping("/received")
    void sendItem(@RequestBody MultiplayerTrackerPayload payload);

    @PutMapping("/chu")
    void sendChu(@RequestBody BlueChuPayload payload);


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class BlueChuPayload {
        private String chuName;
        private String playerName;
        private String gameRoom;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class MultiplayerTrackerPayload {
        private Byte itemId;
        private String checkName;
        private String playerName;
        private String gameRoom;
    }
}
