package crain.util.interceptor;

import crain.exceptions.InvalidGameRoomException;
import crain.service.GameRoomService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class TopicSubscribeInterceptor implements ChannelInterceptor {

    private final GameRoomService gameRoomService;

    @Override
    @SneakyThrows
    @SuppressWarnings({"unchecked", "ConstantConditions"}) // IDE Doesn't register the Try/Catch, smh
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        try {
            final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            if (StompCommand.CONNECT.equals(accessor.getCommand()) || StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                return message;
            }
            var headers = (LinkedMultiValueMap<String, String>) accessor.getHeader("nativeHeaders");
            if (Objects.nonNull(headers) && headers.containsKey("password") && !Objects.requireNonNull(headers.get("password")).isEmpty()) {
                String password = headers.get("password").getFirst();
                String gameRoomName = extractGameRoomFromURL(accessor.getDestination());
                if (gameRoomService.validateGameRoomPassword(gameRoomName, password)) {
                    return message;
                }
            }
        } catch (NullPointerException ignored) {
            log.debug("Null Pointer Exception Swallowed");
            // We're just going to convert any NullPointerExceptions into an InvalidGameRoomException.
            // Currently means the Same thing. is really just a Styling Choice.
        }
        throw new InvalidGameRoomException("Failed to Communicate with the Game Room.");
    }

    @SneakyThrows
    private String extractGameRoomFromURL(@NonNull String destination) {
        if (destination.length() >= 30) {
            log.debug("Attempted to message: {}", destination);
            throw new InvalidGameRoomException("Invalid Game Room Name!");
        }
        String[] urlBreakdown = destination.split("/");
        if (urlBreakdown.length > 4) {
            log.debug("Invalid Destination Attempted: {}", destination);
            throw new InvalidGameRoomException("Invalid Destination URL");
        }
        return urlBreakdown[urlBreakdown.length - 1];
    }
}
