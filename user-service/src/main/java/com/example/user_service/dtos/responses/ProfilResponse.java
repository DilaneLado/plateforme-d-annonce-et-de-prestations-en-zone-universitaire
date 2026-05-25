package com.example.user_service.dtos.responses;

import com.example.user_service.models.Role;
import com.example.user_service.models.Status;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class ProfilResponse {
    private UUID id;
    private String email;
    private Role role;
    private String nomPrenom;
    private String photo;
    private String universite;
    private String competences;
    private String rccm;
    private String secteur;
    private String description;
    private Status status;
}
