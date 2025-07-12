package com.hien.back_end_app.config;


import com.hien.back_end_app.config.security.CustomJwtDecoder;
import com.hien.back_end_app.entities.User;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.repositories.ConversationRepository;
import com.hien.back_end_app.repositories.UserRepository;
import com.hien.back_end_app.utils.enums.ErrorCode;
import com.hien.back_end_app.utils.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final CustomJwtDecoder customJwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration
                .setMessageSizeLimit(5 * 1024 * 1024)
                .setSendBufferSizeLimit(5 * 1024 * 1024)
                .setSendTimeLimit(20 * 1000);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        // prefix broadcast
        registry.enableSimpleBroker("/topic", "/queue");
        // prefix private
        registry.setUserDestinationPrefix("/users");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    log.info("----------------------connect--------------------");
                    String token = extractTokenFromHeader(accessor.getNativeHeader("Authorization").get(0));
                    if (token != null) {
                        Jwt jwt = customJwtDecoder.decode(token);
                        AbstractAuthenticationToken authentication = jwtAuthenticationConverter.convert(jwt);
                        accessor.setUser(authentication);
                        String email = authentication.getName();
                        setIsOnlineUser(email, true);
                    } else {
                        throw new AppException(ErrorCode.TOKEN_INVALID);
                    }
                } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                    log.info("----------------------subscribe--------------------");
                    String destination = accessor.getDestination();
                    String jwtToken = extractTokenFromHeader(accessor.getNativeHeader("Authorization").get(0));
                    if (jwtToken == null) {
                        throw new AppException(ErrorCode.TOKEN_INVALID);
                    }
                    String email = accessor.getUser().getName();
                    checkPort(email, destination);
                } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                    log.info("----------------------disconnect--------------------");
                    String email = accessor.getUser().getName();
                    setIsOnlineUser(email, false);
                }
                return message;
            }
        });
    }

    private void checkPort(String email, String destination) {
        if (destination.contains("conversation")) {
            int conversationId = Integer.parseInt(destination.substring(destination.lastIndexOf("/") + 1));
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            if (conversationRepository.findByConversationIdAndUserId(conversationId, user.getId()).isEmpty()) {
                throw new AppException(ErrorCode.USER_NOT_SUBSCRIBE);
            }
        }
    }

    private String extractTokenFromHeader(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring("Bearer ".length());
    }

    private void setIsOnlineUser(String email, boolean isOnline) {
        if (isOnline) {
            userRepository.updateOnlineByEmail(email, UserStatus.ONLINE);
        } else {
            userRepository.updateOnlineByEmail(email, UserStatus.OFFLINE);
        }
    }
}
