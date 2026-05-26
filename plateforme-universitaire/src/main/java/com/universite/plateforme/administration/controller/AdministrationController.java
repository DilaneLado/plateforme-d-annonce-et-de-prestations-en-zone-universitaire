package com.universite.plateforme.administration.controller;

import com.universite.plateforme.administration.dto.ModerationRequest;
import com.universite.plateforme.administration.dto.StatistiquesDto;
import com.universite.plateforme.administration.service.AdministrationService;
import com.universite.plateforme.common.enums.StatutPublication;
import com.universite.plateforme.common.response.ApiResponse;
import com.universite.plateforme.common.response.PageResponse;
import com.universite.plateforme.publication.dto.PublicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Administration", description = "Back-office réservé aux administrateurs")
@SecurityRequirement(name = "bearerAuth")
public class AdministrationController {

    private  AdministrationService administrationService;

    public AdministrationController(AdministrationService administrationService) {
        this.administrationService = administrationService;
    }

    // ==================== TABLEAU DE BORD ====================

    @GetMapping("/statistiques")
    @Operation(summary = "Tableau de bord - statistiques générales")
    public ResponseEntity<ApiResponse<StatistiquesDto>> getStatistiques() {
        return ResponseEntity.ok(ApiResponse.success(administrationService.getStatistiques()));
    }

    // ==================== GESTION PUBLICATIONS ====================

    @GetMapping("/publications")
    @Operation(summary = "Lister toutes les publications (admin)")
    public ResponseEntity<ApiResponse<PageResponse<PublicationResponse>>> getAllPublications(
            @RequestParam(required = false) StatutPublication statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<PublicationResponse> result = administrationService.getAllPublications(
                statut, PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/publications/en-moderation")
    @Operation(summary = "Publications en attente de modération")
    public ResponseEntity<ApiResponse<PageResponse<PublicationResponse>>> getEnModeration(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<PublicationResponse> result = administrationService.getPublicationsEnModeration(
                PageRequest.of(page, size, Sort.by("createdAt").ascending()));
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/publications/signalees")
    @Operation(summary = "Publications signalées")
    public ResponseEntity<ApiResponse<PageResponse<PublicationResponse>>> getSignalees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<PublicationResponse> result = administrationService.getPublicationsSignalees(
                PageRequest.of(page, size, Sort.by("nbSignalements").descending()));
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PatchMapping("/publications/{id}/valider")
    @Operation(summary = "Valider une publication (la rendre active)")
    public ResponseEntity<ApiResponse<PublicationResponse>> valider(@PathVariable UUID id) {
        PublicationResponse response = administrationService.validerPublication(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Publication validée et publiée"));
    }

    @PatchMapping("/publications/{id}/rejeter")
    @Operation(summary = "Rejeter une publication")
    public ResponseEntity<ApiResponse<PublicationResponse>> rejeter(
            @PathVariable UUID id,
            @Valid @RequestBody ModerationRequest request) {
        PublicationResponse response = administrationService.rejeterPublication(id, request.getMotif());
        return ResponseEntity.ok(ApiResponse.success(response, "Publication rejetée"));
    }

    @PatchMapping("/publications/{id}/archiver")
    @Operation(summary = "Archiver une publication")
    public ResponseEntity<ApiResponse<PublicationResponse>> archiver(@PathVariable UUID id) {
        PublicationResponse response = administrationService.archiverPublication(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Publication archivée"));
    }

    @DeleteMapping("/publications/{id}")
    @Operation(summary = "Supprimer définitivement une publication")
    public ResponseEntity<ApiResponse<Void>> supprimer(@PathVariable UUID id) {
        administrationService.supprimerPublication(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Publication supprimée définitivement"));
    }
}
