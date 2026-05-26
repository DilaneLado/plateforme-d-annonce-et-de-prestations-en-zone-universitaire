package com.universite.plateforme.recherche.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RechercheResponse {

    private List<ResultatItem> resultats;
    private long total;
    private int page;
    private int size;
    private int totalPages;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultatItem {
        private String id;
        private String type;
        private String titre;
        private String description;
        private String auteurNom;
        private String categorie;
        private String localisation;
        private LocalDateTime createdAt;
        private List<String> highlights;
        private Double score;
    }
}
