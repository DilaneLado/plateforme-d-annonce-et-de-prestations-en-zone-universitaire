package com.universite.publication.mapper;

import com.universite.publication.document.PublicationDocument;
import com.universite.publication.dto.PublicationRequest;
import com.universite.publication.dto.PublicationResponse;
import com.universite.publication.entity.Publication;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PublicationMapper {

    // Publication -> DTO réponse
    PublicationResponse toResponse(Publication publication);

    // DTO requête -> entité  (id, statut, auditing gérés ailleurs)
    @Mapping(target = "statut",         ignore = true)
    @Mapping(target = "nbSignalements", ignore = true)
    @Mapping(target = "estSignalee",    ignore = true)
    @Mapping(target = "createdAt",      ignore = true)
    @Mapping(target = "updatedAt",      ignore = true)
    Publication toEntity(PublicationRequest request);

    // Mise à jour partielle
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "statut",         ignore = true)
    @Mapping(target = "nbSignalements", ignore = true)
    @Mapping(target = "estSignalee",    ignore = true)
    @Mapping(target = "createdAt",      ignore = true)
    @Mapping(target = "updatedAt",      ignore = true)
    void updateFromRequest(PublicationRequest request, @MappingTarget Publication publication);

    // Entité -> document Elasticsearch
    @Mapping(target = "id",       expression = "java(pub.getId() != null ? pub.getId().toString() : null)")
    @Mapping(target = "type",     expression = "java(pub.getType() != null ? pub.getType().name() : null)")
    @Mapping(target = "statut",   expression = "java(pub.getStatut() != null ? pub.getStatut().name() : null)")
    @Mapping(target = "auteurId", expression = "java(pub.getAuteurId() != null ? pub.getAuteurId().toString() : null)")
    PublicationDocument toDocument(Publication pub);
}
