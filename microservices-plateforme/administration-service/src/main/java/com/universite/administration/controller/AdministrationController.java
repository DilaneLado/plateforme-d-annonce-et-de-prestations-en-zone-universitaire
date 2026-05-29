package com.universite.administration.controller;

import com.universite.administration.common.enums.StatutPublication;
import com.universite.administration.common.response.ApiResponse;
import com.universite.administration.common.response.PageResponse;
import com.universite.administration.dto.ModerationRequest;
import com.universite.administration.dto.PublicationResponse;
import com.universite.administration.dto.StatistiquesDto;
import com.universite.administration.service.AdministrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Administration", description = "Back-office réservé aux administrateurs")
public class AdministrationController {

    private final AdministrationService administrationService;

    @GetMapping("/statistiques")
    @Operation(summary = "Tableau de bord - statistiques générales")
    public ResponseEntity<ApiResponse<StatistiquesDto>> getStatistiques() {
        return ResponseEntity.ok(ApiResponse.success(administrationService.getStatistiques()));
    }

    @GetMapping("/publications")
    @Operation(summary = "Lister toutes les publications (admin)")
    public ResponseEntity<ApiResponse<PageResponse<PublicationResponse>>> getAllPublications(
            @RequestParam(required = false) StatutPublication statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
            administrationService.getAllPublications(statut, page, size)));
    }

    @GetMapping("/publications/en-moderation")
    @Operation(summary = "Publications en attente de modération")
    public ResponseEntity<ApiResponse<PageResponse<PublicationResponse>>> getEnModeration(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
            administrationService.getPublicationsEnModeration(page, size)));
    }

    @GetMapping("/publications/signalees")
    @Operation(summary = "Publications signalées")
    public ResponseEntity<ApiResponse<PageResponse<PublicationResponse>>> getSignalees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
            administrationService.getPublicationsSignalees(page, size)));
    }

    @PatchMapping("/publications/{id}/valider")
    @Operation(summary = "Valider une publication")
    public ResponseEntity<ApiResponse<PublicationResponse>> valider(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(
            administrationService.validerPublication(id), "Publication validée et publiée"));
    }

    @PatchMapping("/publications/{id}/rejeter")
    @Operation(summary = "Rejeter une publication")
    public ResponseEntity<ApiResponse<PublicationResponse>> rejeter(
            @PathVariable String id,
            @Valid @RequestBody ModerationRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
            administrationService.rejeterPublication(id, request.getMotif()), "Publication rejetée"));
    }

    @PatchMapping("/publications/{id}/archiver")
    @Operation(summary = "Archiver une publication")
    public ResponseEntity<ApiResponse<PublicationResponse>> archiver(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(
            administrationService.archiverPublication(id), "Publication archivée"));
    }

    @DeleteMapping("/publications/{id}")
    @Operation(summary = "Supprimer définitivement une publication")
    public ResponseEntity<ApiResponse<Void>> supprimer(@PathVariable String id) {
        administrationService.supprimerPublication(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Publication supprimée définitivement"));
    }
}
