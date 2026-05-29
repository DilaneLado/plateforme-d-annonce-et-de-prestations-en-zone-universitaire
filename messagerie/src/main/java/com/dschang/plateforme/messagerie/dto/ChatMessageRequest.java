package com.dschang.plateforme.messagerie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatMessageRequest {
    @NotBlank(message = "L'ID de la conversation est obligatoire")
    private String conversationId;

    @NotBlank(message = "Le contenu du message est obligatoire")
    private String contenu;

    
}