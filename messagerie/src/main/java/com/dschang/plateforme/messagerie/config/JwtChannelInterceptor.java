package com.dschang.plateforme.messagerie.config;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

   @Override
public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
        System.out.println(">>> Intercepteur CONNECT appelé");
        String authHeader = accessor.getFirstNativeHeader("Authorization");
        System.out.println(">>> Header Authorization reçu : " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println(">>> Token extrait : " + token);
            boolean valid = jwtTokenProvider.validateToken(token);
            System.out.println(">>> Token valide ? " + valid);
            if (valid) {
                var userId = jwtTokenProvider.getUserIdFromToken(token).toString();
                System.out.println(">>> Utilisateur ID : " + userId);
                var auth = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);
                accessor.setUser(auth);
                System.out.println(">>> Authentification définie sur la session");
            }
        } else {
            System.out.println(">>> Pas de header Authorization valide");
        }
    }
    return message;
}
}