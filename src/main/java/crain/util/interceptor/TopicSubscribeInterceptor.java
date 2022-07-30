package crain.util.interceptor;

import crain.exceptions.InvalidGameRoomException;
import crain.service.GameRoomService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TopicSubscribeInterceptor implements ChannelInterceptor {

    @NotNull
    private final GameRoomService gameRoomService;

    @Override
    @SneakyThrows
    @SuppressWarnings({"unchecked", "ConstantConditions"}) // IDE Doesn't register the Try/Catch, smh
    public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
        try {
            final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            if (StompCommand.CONNECT.equals(accessor.getCommand()) || StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                return message;
            }
            var headers = (LinkedMultiValueMap<String, String>) accessor.getHeader("nativeHeaders");
            if (Objects.nonNull(headers) && headers.containsKey("password") && 1 <= (Objects.requireNonNull(headers.get("password")).size())) {
                String password = headers.get("password").get(0);
                String gameRoomName = extractGameRoomFromURL(accessor.getDestination());
                if (gameRoomService.validateGameRoomPassword(gameRoomName, password)) {
                    return message;
                }
            }
        } catch (NullPointerException ignored) {
            // We're just going to convert any NullPointerExceptions into an InvalidGameRoomException.
            // Currently means the Same thing. is really just a Styling Choice.
        }
        throw new InvalidGameRoomException("Failed to Communicate with the Gameroom.");
    }

    @SneakyThrows
    private String extractGameRoomFromURL(@NonNull String destination) {
        if (destination.length() >= 30) {
            throw new InvalidGameRoomException("Invalid Gameroom Name!");
        }
        String[] urlBreakdown = destination.split("/");
        if (urlBreakdown.length > 4) {
            throw new InvalidGameRoomException("Invalid Destination URL");
        }
        return urlBreakdown[urlBreakdown.length - 1];
    }
}
