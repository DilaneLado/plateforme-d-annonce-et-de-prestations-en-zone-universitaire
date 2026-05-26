package com.universite.plateforme.publication.service;

import com.universite.plateforme.common.enums.StatutPublication;
import com.universite.plateforme.common.enums.TypePublication;
import com.universite.plateforme.common.exception.AccessDeniedException;
import com.universite.plateforme.common.exception.ResourceNotFoundException;
import com.universite.plateforme.common.response.PageResponse;
import com.universite.plateforme.publication.document.PublicationDocument;
import com.universite.plateforme.publication.dto.PublicationRequest;
import com.universite.plateforme.publication.dto.PublicationResponse;
import com.universite.plateforme.publication.dto.PublicationUpdateRequest;
import com.universite.plateforme.publication.entity.Publication;
import com.universite.plateforme.publication.mapper.PublicationMapper;
import com.universite.plateforme.publication.repository.PublicationRepository;
import com.universite.plateforme.publication.repository.PublicationSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicationService {

    private final PublicationRepository publicationRepository;
    private final PublicationSearchRepository searchRepository;
    private final PublicationMapper publicationMapper;

    // ==================== CRUD ====================

    @Transactional
    public PublicationResponse creer(PublicationRequest request, UUID auteurId, String auteurNom, String auteurRole) {

        // ✅ Mapping manuel (contourne le MapperImpl vide)
        Publication publication = new Publication();
        publication.setType(request.getType());
        publication.setTitre(request.getTitre());
        publication.setDescription(request.getDescription());
        publication.setCategorie(request.getCategorie());
        publication.setLocalisation(request.getLocalisation());
        publication.setDateExpiration(request.getDateExpiration());
        publication.setMedias(request.getMedias() != null ? request.getMedias() : new ArrayList<>());
        publication.setAuteurId(auteurId);
        publication.setAuteurNom(auteurNom);
        publication.setAuteurRole(auteurRole);

        Publication saved = publicationRepository.save(publication);
        indexerDansElasticsearch(saved);

        log.info("Publication créée : {} par {}", saved.getId(), auteurNom);
        return toResponse(saved);  // voir ci-dessous
    }

    public PublicationResponse trouverParId(UUID id) {
        return publicationMapper.toResponse(getPublicationOuErreur(id));
    }

    @Transactional
    public PublicationResponse modifier(UUID id, PublicationUpdateRequest request, UUID auteurId, boolean isAdmin) {
        Publication publication = getPublicationOuErreur(id);

        if (!isAdmin && !publication.getAuteurId().equals(auteurId)) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à modifier cette publication");
        }

        if (request.getTitre() != null) publication.setTitre(request.getTitre());
        if (request.getDescription() != null) publication.setDescription(request.getDescription());
        if (request.getCategorie() != null) publication.setCategorie(request.getCategorie());
        if (request.getMedias() != null) publication.setMedias(request.getMedias());
        if (request.getLocalisation() != null) publication.setLocalisation(request.getLocalisation());
        if (request.getDateExpiration() != null) publication.setDateExpiration(request.getDateExpiration());

        Publication updated = publicationRepository.save(publication);
        indexerDansElasticsearch(updated);

        return publicationMapper.toResponse(updated);
    }

    @Transactional
    public void supprimer(UUID id, UUID auteurId, boolean isAdmin) {
        Publication publication = getPublicationOuErreur(id);

        if (!isAdmin && !publication.getAuteurId().equals(auteurId)) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à supprimer cette publication");
        }

        publicationRepository.delete(publication);
        searchRepository.deleteById(id.toString());
        log.info("Publication supprimée : {}", id);
    }

    // ==================== LECTURE / FILTRES ====================

    public PageResponse<PublicationResponse> listerActives(TypePublication type, String categorie, String localisation, Pageable pageable) {
        Page<Publication> page = publicationRepository.findWithFilters(
                StatutPublication.ACTIVE, type, categorie, localisation, pageable);
        return PageResponse.of(page.map(publicationMapper::toResponse));
    }

    public PageResponse<PublicationResponse> listerParAuteur(UUID auteurId, Pageable pageable) {
        Page<Publication> page = publicationRepository.findByAuteurId(auteurId, pageable);
        return PageResponse.of(page.map(publicationMapper::toResponse));
    }

    // ==================== SIGNALEMENT ====================

    @Transactional
    public void signalerPublication(UUID id) {
        if (!publicationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Publication", "id", id);
        }
        publicationRepository.incrementerSignalement(id);
        log.info("Publication signalée : {}", id);
    }

    // ==================== ADMINISTRATION ====================

    @Transactional
    public PublicationResponse changerStatut(UUID id, StatutPublication nouveauStatut) {
        Publication publication = getPublicationOuErreur(id);
        publication.setStatut(nouveauStatut);
        Publication updated = publicationRepository.save(publication);

        if (nouveauStatut == StatutPublication.ACTIVE) {
            indexerDansElasticsearch(updated);
        } else {
            searchRepository.deleteById(id.toString());
        }

        log.info("Statut publication {} changé en {}", id, nouveauStatut);
        return publicationMapper.toResponse(updated);
    }

    public PageResponse<PublicationResponse> listerEnModeration(Pageable pageable) {
        Page<Publication> page = publicationRepository.findByStatut(StatutPublication.EN_MODERATION, pageable);
        return PageResponse.of(page.map(publicationMapper::toResponse));
    }

    public PageResponse<PublicationResponse> listerSignalees(Pageable pageable) {
        Page<Publication> page = publicationRepository.findByEstSignaleeTrue(pageable);
        return PageResponse.of(page.map(publicationMapper::toResponse));
    }

    // ==================== UTILITAIRES ====================

    private Publication getPublicationOuErreur(UUID id) {
        return publicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publication", "id", id));
    }

    private void indexerDansElasticsearch(Publication publication) {
        try {
            PublicationDocument doc = publicationMapper.toDocument(publication);
            searchRepository.save(doc);
        } catch (Exception e) {
            log.error("Erreur lors de l'indexation Elasticsearch de la publication {} : {}", publication.getId(), e.getMessage());
        }
    }

    private PublicationResponse toResponse(Publication p) {
        PublicationResponse r = new PublicationResponse();
        r.setId(p.getId());
        r.setType(p.getType());
        r.setTitre(p.getTitre());
        r.setDescription(p.getDescription());
        r.setCategorie(p.getCategorie());
        r.setLocalisation(p.getLocalisation());
        r.setStatut(p.getStatut());
        r.setAuteurId(p.getAuteurId());
        r.setAuteurNom(p.getAuteurNom());
        r.setAuteurRole(p.getAuteurRole());
        r.setMedias(p.getMedias());
        r.setDateExpiration(p.getDateExpiration());
        r.setCreatedAt(p.getCreatedAt());
        r.setUpdatedAt(p.getUpdatedAt());
        r.setNbSignalements(p.getNbSignalements());
        r.setEstSignalee(p.getEstSignalee());
        return r;
    }
}
