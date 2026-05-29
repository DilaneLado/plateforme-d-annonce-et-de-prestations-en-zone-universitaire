package com.universite.publication.controller;

import com.universite.publication.common.enums.StatutPublication;
import com.universite.publication.common.enums.TypePublication;
import com.universite.publication.common.response.ApiResponse;
import com.universite.publication.common.response.PageResponse;
import com.universite.publication.dto.PublicationRequest;
import com.universite.publication.dto.PublicationResponse;
import com.universite.publication.dto.PublicationUpdateRequest;
import com.universite.publication.security.UserPrincipal;
import com.universite.publication.service.PublicationService;
import io.swagger.v3.oas.annotations.Operation;
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
            .body(ApiResponse.success(response, "Publication créée, en attente de modération"));
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

        boolean isAdmin = "ADMIN".equals(principal.getRole());
        PublicationResponse response = publicationService.modifier(id, request, principal.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(response, "Publication modifiée avec succès"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une publication")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> supprimer(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {

        boolean isAdmin = "ADMIN".equals(principal.getRole());
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

    // ==================== Endpoints internes (appelés par administration-service) ====================

    @PatchMapping("/internal/{id}/statut")
    @Operation(summary = "[Interne] Changer le statut d'une publication")
    public ResponseEntity<ApiResponse<PublicationResponse>> changerStatut(
            @PathVariable UUID id,
            @RequestParam StatutPublication statut) {
        return ResponseEntity.ok(ApiResponse.success(publicationService.changerStatut(id, statut)));
    }

    @GetMapping("/internal/en-moderation")
    @Operation(summary = "[Interne] Publications en modération")
    public ResponseEntity<ApiResponse<PageResponse<PublicationResponse>>> enModeration(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
            publicationService.listerEnModeration(PageRequest.of(page, size, Sort.by("createdAt").ascending()))));
    }

    @GetMapping("/internal/signalees")
    @Operation(summary = "[Interne] Publications signalées")
    public ResponseEntity<ApiResponse<PageResponse<PublicationResponse>>> signalees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
            publicationService.listerSignalees(PageRequest.of(page, size, Sort.by("nbSignalements").descending()))));
    }

    @GetMapping("/internal/count")
    @Operation(summary = "[Interne] Compter les publications")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(publicationService.count());
    }

    @GetMapping("/internal/count-by-statut")
    @Operation(summary = "[Interne] Compter par statut")
    public ResponseEntity<Long> countByStatut(@RequestParam StatutPublication statut) {
        return ResponseEntity.ok(publicationService.countByStatut(statut));
    }
}
