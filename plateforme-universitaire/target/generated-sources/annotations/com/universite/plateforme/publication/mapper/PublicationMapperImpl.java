package com.universite.plateforme.publication.mapper;

import com.universite.plateforme.publication.document.PublicationDocument;
import com.universite.plateforme.publication.dto.PublicationRequest;
import com.universite.plateforme.publication.dto.PublicationResponse;
import com.universite.plateforme.publication.entity.Publication;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-26T05:38:28+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class PublicationMapperImpl implements PublicationMapper {

    @Override
    public PublicationResponse toResponse(Publication publication) {
        if ( publication == null ) {
            return null;
        }

        PublicationResponse publicationResponse = new PublicationResponse();

        return publicationResponse;
    }

    @Override
    public Publication toEntity(PublicationRequest request) {
        if ( request == null ) {
            return null;
        }

        Publication publication = new Publication();

        return publication;
    }

    @Override
    public void updateFromRequest(PublicationRequest request, Publication publication) {
        if ( request == null ) {
            return;
        }
    }

    @Override
    public PublicationDocument toDocument(Publication pub) {
        if ( pub == null ) {
            return null;
        }

        PublicationDocument publicationDocument = new PublicationDocument();

        publicationDocument.setTitre( pub.getTitre() );
        publicationDocument.setDescription( pub.getDescription() );
        publicationDocument.setAuteurNom( pub.getAuteurNom() );
        publicationDocument.setCategorie( pub.getCategorie() );
        publicationDocument.setLocalisation( pub.getLocalisation() );
        publicationDocument.setDateExpiration( pub.getDateExpiration() );
        publicationDocument.setCreatedAt( pub.getCreatedAt() );

        publicationDocument.setId( pub.getId() != null ? pub.getId().toString() : null );
        publicationDocument.setType( pub.getType() != null ? pub.getType().name() : null );
        publicationDocument.setStatut( pub.getStatut() != null ? pub.getStatut().name() : null );
        publicationDocument.setAuteurId( pub.getAuteurId() != null ? pub.getAuteurId().toString() : null );

        return publicationDocument;
    }
}
