package crain.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Slf4j
@Controller
@ConditionalOnProperty(name = "enable.subscription.controller", havingValue = "true")
public class SubscriptionController {


    public SubscriptionController() {
        log.info("Subscription Controller Created");
    }

    @SubscribeMapping("/coop/{GameRoom}")
    public void subscribeToItemQueue(@DestinationVariable("GameRoom") String gameRoomName,
                                     @Headers Map<String, String> headers) {
        log.info("Subscription to: " + gameRoomName + " Destination: " + headers.get("destination"));
    }

    @SubscribeMapping("/coop/item/{GameRoom}")
    public void subscribeToTopicItemQueue(@DestinationVariable("GameRoom") String gameRoomName,
                                     @Headers Map<String, String> headers) {
        log.info("Topic Subscription to: " + gameRoomName + " Destination: " + headers.get("destination"));
    }

}
