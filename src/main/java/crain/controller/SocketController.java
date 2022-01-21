package crain.controller;

import crain.model.dto.ItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SocketController {

    @MessageMapping("/item")
    public ItemDto postItemToGlobalTopic(@Payload ItemDto input) {
        return input;
    }
}
