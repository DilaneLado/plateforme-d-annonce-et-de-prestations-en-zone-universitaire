package com.dschang.plateforme.messagerie.dto;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MessageResponse {
    private String id;
    private String conversationId;
    private UUID expediteurId;
    private String contenu;
    private Instant timestamp;
    private boolean lu;
}