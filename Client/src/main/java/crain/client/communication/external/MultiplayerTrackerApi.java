package crain.client.communication.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PutExchange;


public interface MultiplayerTrackerApi {


    @PutExchange("/received")
    void sendItem(@RequestBody MultiplayerTrackerPayload payload);

    @PutExchange("/chu")
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
