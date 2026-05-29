package com.dschang.plateforme.messagerie.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Document(collection = "conversations")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Conversation {
    @Id
    private String id;
    private List<UUID> participants;  // exactement 2 participants
    private UUID annonceId;           // nullable
    private Instant derniereActivite;
}