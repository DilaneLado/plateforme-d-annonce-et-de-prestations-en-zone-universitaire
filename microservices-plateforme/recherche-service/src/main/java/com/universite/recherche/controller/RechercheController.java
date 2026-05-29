package com.universite.recherche.controller;

import com.universite.recherche.common.response.ApiResponse;
import com.universite.recherche.dto.RechercheRequest;
import com.universite.recherche.dto.RechercheResponse;
import com.universite.recherche.service.RechercheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recherche")
@RequiredArgsConstructor
@Tag(name = "Recherche", description = "Recherche full-text via Elasticsearch")
public class RechercheController {

    private final RechercheService rechercheService;

    @GetMapping
    @Operation(summary = "Rechercher des publications", description = "Recherche full-text avec filtres sur titre, description, type, catégorie, localisation")
    public ResponseEntity<ApiResponse<RechercheResponse>> rechercher(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String categorie,
            @RequestParam(required = false) String localisation,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        RechercheRequest request = new RechercheRequest();
        request.setQ(q);
        request.setType(type);
        request.setCategorie(categorie);
        request.setLocalisation(localisation);
        request.setPage(page);
        request.setSize(size);

        RechercheResponse response = rechercheService.rechercher(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
