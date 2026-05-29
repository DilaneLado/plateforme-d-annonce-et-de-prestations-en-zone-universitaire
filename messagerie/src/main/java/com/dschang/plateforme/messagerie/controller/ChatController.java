package com.dschang.plateforme.messagerie.controller;

import com.dschang.plateforme.messagerie.dto.ChatMessageRequest;
import com.dschang.plateforme.messagerie.dto.MessageResponse;
import com.dschang.plateforme.messagerie.service.MessagerieService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessagerieService messagerieService;

    // Envoyer un message : destination /app/chat
    @MessageMapping("/chat")
public void handleChat(@Payload ChatMessageRequest request, Principal principal) {
    if (principal == null) {
        throw new IllegalStateException("Utilisateur non authentifié");
    }
    UUID expediteurId = UUID.fromString(principal.getName());

    MessageResponse response = messagerieService.sendMessage(
            request.getConversationId(),
            expediteurId,
            request.getContenu()
    );

    messagingTemplate.convertAndSend(
            "/topic/conversation/" + response.getConversationId(),
            response
    );
}

    // Indicateur de frappe : destination /app/typing
    @MessageMapping("/typing")
    public void handleTyping(@Payload ChatMessageRequest request, Principal principal) {
        UUID expediteurId = UUID.fromString(principal.getName());
        // On envoie juste l'info de qui est en train d'écrire
        messagingTemplate.convertAndSend(
                "/topic/conversation/" + request.getConversationId() + ".typing",
                expediteurId.toString()
        );
    }

    // Marquer un message comme lu : destination /app/read
    @MessageMapping("/read")
    public void handleRead(@Payload MessageResponse readRequest, Principal principal) {
        UUID readerId = UUID.fromString(principal.getName());
        messagerieService.markAsRead(readRequest.getId(), readerId);

        // Notifier que ce message a été lu
        messagingTemplate.convertAndSend(
                "/topic/conversation/" + readRequest.getConversationId() + ".read",
                readRequest.getId()
        );
    }
}