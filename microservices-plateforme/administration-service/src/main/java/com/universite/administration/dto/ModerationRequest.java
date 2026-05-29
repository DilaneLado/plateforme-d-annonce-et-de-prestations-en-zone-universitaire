package com.universite.administration.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ModerationRequest {

    @NotBlank(message = "Le motif est obligatoire")
    private String motif;
}
