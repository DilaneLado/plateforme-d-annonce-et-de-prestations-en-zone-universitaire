package com.universite.plateforme.publication.dto;

import com.universite.plateforme.common.enums.TypePublication;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PublicationRequest {

    @NotNull(message = "Le type est obligatoire")
    private TypePublication type;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 100, message = "Le titre ne peut pas dépasser 100 caractères")
    private String titre;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @Size(max = 100, message = "La catégorie ne peut pas dépasser 100 caractères")
    private String categorie;

    private List<String> medias;

    @Size(max = 150, message = "La localisation ne peut pas dépasser 150 caractères")
    private String localisation;

    private LocalDate dateExpiration;
}
