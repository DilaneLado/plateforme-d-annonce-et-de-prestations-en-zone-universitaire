package com.dschang.plateforme.messagerie.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.UUID;

@Document(collection = "messages")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Message {
    @Id
    private String id;
    private String conversationId;
    private UUID expediteurId;
    private String contenu;
    private Instant timestamp;
    private boolean lu;
}