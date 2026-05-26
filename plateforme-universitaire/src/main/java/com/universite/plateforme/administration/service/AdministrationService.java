package com.universite.plateforme.administration.service;

import com.universite.plateforme.administration.dto.StatistiquesDto;
import com.universite.plateforme.common.enums.StatutPublication;
import com.universite.plateforme.common.enums.TypePublication;
import com.universite.plateforme.common.response.PageResponse;
import com.universite.plateforme.publication.dto.PublicationResponse;
import com.universite.plateforme.publication.repository.PublicationRepository;
import com.universite.plateforme.publication.service.PublicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdministrationService {

    private final PublicationService publicationService;
    private final PublicationRepository publicationRepository;

    // ==================== STATISTIQUES ====================

    public StatistiquesDto getStatistiques() {
        long total = publicationRepository.count();
        long actives = publicationRepository.countByStatut(StatutPublication.ACTIVE);
        long enModeration = publicationRepository.countByStatut(StatutPublication.EN_MODERATION);
        long archivees = publicationRepository.countByStatut(StatutPublication.ARCHIVEE);
        long signalees = publicationRepository.findByEstSignaleeTrue(Pageable.unpaged()).getTotalElements();

        List<Object[]> parType = publicationRepository.countByTypeGrouped();
        Map<String, Long> publicationsParType = new HashMap<>();
        for (Object[] row : parType) {
            publicationsParType.put(((TypePublication) row[0]).name(), (Long) row[1]);
        }

        return StatistiquesDto.builder()
                .totalPublications(total)
                .publicationsActives(actives)
                .publicationsEnModeration(enModeration)
                .publicationsArchivees(archivees)
                .publicationsSignalees(signalees)
                .publicationsParType(publicationsParType)
                .build();
    }

    // ==================== MODÉRATION PUBLICATIONS ====================

    public PageResponse<PublicationResponse> getPublicationsEnModeration(Pageable pageable) {
        return publicationService.listerEnModeration(pageable);
    }

    public PageResponse<PublicationResponse> getPublicationsSignalees(Pageable pageable) {
        return publicationService.listerSignalees(pageable);
    }

    @Transactional
    public PublicationResponse validerPublication(UUID publicationId) {
        log.info("Admin : validation de la publication {}", publicationId);
        return publicationService.changerStatut(publicationId, StatutPublication.ACTIVE);
    }

    @Transactional
    public PublicationResponse rejeterPublication(UUID publicationId, String motif) {
        log.info("Admin : rejet de la publication {} - motif : {}", publicationId, motif);
        return publicationService.changerStatut(publicationId, StatutPublication.ARCHIVEE);
    }

    @Transactional
    public PublicationResponse archiverPublication(UUID publicationId) {
        log.info("Admin : archivage de la publication {}", publicationId);
        return publicationService.changerStatut(publicationId, StatutPublication.ARCHIVEE);
    }

    @Transactional
    public void supprimerPublication(UUID publicationId) {
        log.info("Admin : suppression de la publication {}", publicationId);
        publicationService.supprimer(publicationId, null, true);
    }

    public PageResponse<PublicationResponse> getAllPublications(StatutPublication statut, Pageable pageable) {
        if (statut != null) {
            return publicationService.listerEnModeration(pageable); // adapter si besoin
        }
        return publicationService.listerActives(null, null, null, pageable);
    }
}
