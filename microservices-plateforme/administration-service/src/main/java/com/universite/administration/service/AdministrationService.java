package com.universite.administration.service;

import com.universite.administration.client.PublicationClient;
import com.universite.administration.common.enums.StatutPublication;
import com.universite.administration.common.response.PageResponse;
import com.universite.administration.dto.PublicationResponse;
import com.universite.administration.dto.StatistiquesDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdministrationService {

    private final PublicationClient publicationClient;

    // ==================== STATISTIQUES ====================

    public StatistiquesDto getStatistiques() {
        long total        = safeCount(publicationClient.count());
        long actives      = safeCount(publicationClient.countByStatut(StatutPublication.ACTIVE));
        long enModeration = safeCount(publicationClient.countByStatut(StatutPublication.EN_MODERATION));
        long archivees    = safeCount(publicationClient.countByStatut(StatutPublication.ARCHIVEE));

        PageResponse<PublicationResponse> signaleesPage =
            publicationClient.getSignalees(0, 1).getData();
        long signalees = signaleesPage != null ? signaleesPage.getTotalElements() : 0;

        return StatistiquesDto.builder()
            .totalPublications(total)
            .publicationsActives(actives)
            .publicationsEnModeration(enModeration)
            .publicationsArchivees(archivees)
            .publicationsSignalees(signalees)
            .publicationsParType(new HashMap<>())   // à enrichir si besoin
            .build();
    }

    // ==================== MODÉRATION ====================

    public PageResponse<PublicationResponse> getPublicationsEnModeration(int page, int size) {
        return publicationClient.getEnModeration(page, size).getData();
    }

    public PageResponse<PublicationResponse> getPublicationsSignalees(int page, int size) {
        return publicationClient.getSignalees(page, size).getData();
    }

    public PublicationResponse validerPublication(String publicationId) {
        log.info("Admin : validation de la publication {}", publicationId);
        return publicationClient.changerStatut(publicationId, StatutPublication.ACTIVE).getData();
    }

    public PublicationResponse rejeterPublication(String publicationId, String motif) {
        log.info("Admin : rejet de la publication {} - motif : {}", publicationId, motif);
        return publicationClient.changerStatut(publicationId, StatutPublication.ARCHIVEE).getData();
    }

    public PublicationResponse archiverPublication(String publicationId) {
        log.info("Admin : archivage de la publication {}", publicationId);
        return publicationClient.changerStatut(publicationId, StatutPublication.ARCHIVEE).getData();
    }

    public void supprimerPublication(String publicationId) {
        log.info("Admin : suppression de la publication {}", publicationId);
        publicationClient.supprimer(publicationId);
    }

    public PageResponse<PublicationResponse> getAllPublications(StatutPublication statut, int page, int size) {
        return publicationClient.getAll(page, size).getData();
    }

    // ==================== UTILS ====================

    private long safeCount(Long value) {
        return value != null ? value : 0L;
    }
}
