package com.universite.plateforme.publication.controller;

import com.universite.plateforme.common.enums.StatutPublication;
import com.universite.plateforme.common.enums.TypePublication;
import com.universite.plateforme.common.response.ApiResponse;
import com.universite.plateforme.common.response.PageResponse;
import com.universite.plateforme.publication.dto.PublicationRequest;
import com.universite.plateforme.publication.dto.PublicationResponse;
import com.universite.plateforme.publication.dto.PublicationUpdateRequest;
import com.universite.plateforme.publication.service.PublicationService;
import com.universite.plateforme.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/publications")
@RequiredArgsConstructor
@Tag(name = "Publications", description = "Gestion des annonces et publications")
@SecurityRequirement(name = "bearerAuth")
public class PublicationController {

    private final PublicationService publicationService;

    @PostMapping
    @Operation(summary = "Créer une publication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PublicationResponse>> creer(
            @Valid @RequestBody PublicationRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        PublicationResponse response = publicationService.creer(
                request, principal.getId(), principal.getNom(), principal.getRole());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Publication créée avec succès, en attente de modération"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une publication par ID")
    public ResponseEntity<ApiResponse<PublicationResponse>> trouverParId(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(publicationService.trouverParId(id)));
    }

    @GetMapping
    @Operation(summary = "Lister les publications actives avec filtres")
    public ResponseEntity<ApiResponse<PageResponse<PublicationResponse>>> lister(
            @RequestParam(required = false) TypePublication type,
            @RequestParam(required = false) String categorie,
            @RequestParam(required = false) String localisation,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageResponse<PublicationResponse> result = publicationService.listerActives(
                type, categorie, localisation, PageRequest.of(page, size, sort));
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/mes-publications")
    @Operation(summary = "Mes publications")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PageResponse<PublicationResponse>>> mesPublications(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<PublicationResponse> result = publicationService.listerParAuteur(
                principal.getId(), PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une publication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PublicationResponse>> modifier(
            @PathVariable UUID id,
            @Valid @RequestBody PublicationUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        boolean isAdmin = principal.getRole().equals("ADMIN");
        PublicationResponse response = publicationService.modifier(id, request, principal.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(response, "Publication modifiée avec succès"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une publication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> supprimer(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {

        boolean isAdmin = principal.getRole().equals("ADMIN");
        publicationService.supprimer(id, principal.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "Publication supprimée avec succès"));
    }

    @PostMapping("/{id}/signaler")
    @Operation(summary = "Signaler une publication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> signaler(@PathVariable UUID id) {
        publicationService.signalerPublication(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Publication signalée"));
    }
}
