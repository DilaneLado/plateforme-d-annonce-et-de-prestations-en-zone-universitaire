package com.dschang.plateforme.messagerie.controller;

import com.dschang.plateforme.messagerie.dto.ConversationResponse;
import com.dschang.plateforme.messagerie.service.MessagerieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ChatRestController {

    private final MessagerieService messagerieService;

    // Créer une conversation entre deux utilisateurs
    @PostMapping
    public ResponseEntity<ConversationResponse> createConversation(@RequestBody Map<String, String> body) {
        UUID participant1 = UUID.fromString(body.get("participant1"));
        UUID participant2 = UUID.fromString(body.get("participant2"));
        UUID annonceId = body.containsKey("annonceId") ? UUID.fromString(body.get("annonceId")) : null;

        ConversationResponse response = messagerieService.createOrGetConversation(participant1, participant2, annonceId);
        return ResponseEntity.ok(response);
    }

    // Lister les conversations d'un utilisateur (optionnel pour test)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ConversationResponse>> getUserConversations(@PathVariable UUID userId) {
        return ResponseEntity.ok(messagerieService.getUserConversations(userId));
    }
}