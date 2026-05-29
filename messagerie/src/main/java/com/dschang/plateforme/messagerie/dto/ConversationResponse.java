package com.dschang.plateforme.messagerie.dto;

import lombok.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ConversationResponse {
    private String id;
    private List<UUID> participants;
    private UUID annonceId;
    private Instant derniereActivite;
    private MessageResponse dernierMessage; // optionnel
}