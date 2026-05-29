package com.universite.administration.client;

import com.universite.administration.common.enums.StatutPublication;
import com.universite.administration.common.response.ApiResponse;
import com.universite.administration.common.response.PageResponse;
import com.universite.administration.dto.PublicationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Client Feign pour appeler publication-service via Eureka.
 * Utilise les endpoints /internal/* exposés uniquement pour la communication inter-services.
 */
@FeignClient(name = "publication-service", path = "/api/v1/publications")
public interface PublicationClient {

    @PatchMapping("/internal/{id}/statut")
    ApiResponse<PublicationResponse> changerStatut(
        @PathVariable("id") String id,
        @RequestParam("statut") StatutPublication statut);

    @GetMapping("/internal/en-moderation")
    ApiResponse<PageResponse<PublicationResponse>> getEnModeration(
        @RequestParam("page") int page,
        @RequestParam("size") int size);

    @GetMapping("/internal/signalees")
    ApiResponse<PageResponse<PublicationResponse>> getSignalees(
        @RequestParam("page") int page,
        @RequestParam("size") int size);

    @GetMapping
    ApiResponse<PageResponse<PublicationResponse>> getAll(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size);

    @DeleteMapping("/{id}")
    ApiResponse<Void> supprimer(@PathVariable("id") String id);

    @GetMapping("/internal/count")
    Long count();

    @GetMapping("/internal/count-by-statut")
    Long countByStatut(@RequestParam("statut") StatutPublication statut);
}
