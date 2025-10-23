package nesalmanov.ru.chatservice.websocket;


import io.micrometer.common.lang.NonNull;
import lombok.extern.slf4j.Slf4j;
import nesalmanov.ru.chatservice.jwt.JwtUtils;
import nesalmanov.ru.chatservice.model.impl.TokenDetails;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Slf4j
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtils jwtUtils;

    public WebSocketAuthInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        try {
            StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

            if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                String authHeader = accessor.getFirstNativeHeader("Authorization");

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    String username = jwtUtils.extractUsername(token);
                    log.info(token);

                    if (username != null
                            && jwtUtils.validateToken(token)
                            && accessor.getUser() == null) {

                        TokenDetails tokenDetails = new TokenDetails(jwtUtils.extractUUID(token), username);

                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(tokenDetails,
                                        null,
                                        Collections.singleton(new SimpleGrantedAuthority("USER")));

                        accessor.setUser(authenticationToken);
                    } else {
//                    throw new IllegalArgumentException("JWT is not valid or user already authenticated");
                        log.warn("JWT is not valid or user already authenticated");

                    }
                } else {
                    log.warn("Authorization header is missing or invalid");
                    return null;
                }
            } else {
                if (accessor != null) {
                    if (accessor.getUser() == null) {
                        log.warn("Not authenticated");
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            log.error("WebSocket authentication error", e);
            return null;
        }
        return message;
    }
}
