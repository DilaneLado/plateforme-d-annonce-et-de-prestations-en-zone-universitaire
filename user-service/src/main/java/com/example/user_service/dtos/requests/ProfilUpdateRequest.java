package com.example.user_service.dtos.requests;

import lombok.Data;

@Data
public class ProfilUpdateRequest {
    private String nomPrenom;
    private String photo;
    private String universite;
    private String competences;
    private String secteur;
    private String description;
}