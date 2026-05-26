package com.universite.plateforme.recherche.dto;

import lombok.Data;

@Data
public class RechercheRequest {
    private String q;
    private String type;
    private String categorie;
    private String localisation;
    private int page = 0;
    private int size = 10;
}
