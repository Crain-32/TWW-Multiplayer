package crain.interceptor;

import crain.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GameRoomSubscribeInterceptor implements ChannelInterceptor {

    private final GameRoomService gameRoomService;

    @Override
    public Message<?> preSend(@NotNull Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (Objects.nonNull(accessor) && StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            var headers = (LinkedMultiValueMap<String, String>) accessor.getHeader("nativeHeaders");
            if (Objects.nonNull(headers) && headers.containsKey("password") && 1 <= (Objects.requireNonNull(headers.get("password")).size())) {
                String password = headers.get("password").get(0);
                String[] urlBreakdown = Objects.requireNonNull(accessor.getDestination()).split("/");
                System.out.println(urlBreakdown[urlBreakdown.length - 1]);
                String existing_password = gameRoomService.getGameRoomPassword(urlBreakdown[urlBreakdown.length - 1]);
                if (existing_password.contentEquals(password)) {
                    return message;
                }
            } else {
                return null;
            }
        } else {
            return message;
        }
        return message;
    }
}
