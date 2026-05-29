package com.universite.publication.mapper;

import com.universite.publication.document.PublicationDocument;
import com.universite.publication.dto.PublicationRequest;
import com.universite.publication.dto.PublicationResponse;
import com.universite.publication.entity.Publication;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-29T07:11:40+0100",
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

        publicationResponse.setId( publication.getId() );
        publicationResponse.setType( publication.getType() );
        publicationResponse.setTitre( publication.getTitre() );
        publicationResponse.setDescription( publication.getDescription() );
        publicationResponse.setAuteurId( publication.getAuteurId() );
        publicationResponse.setAuteurNom( publication.getAuteurNom() );
        publicationResponse.setAuteurRole( publication.getAuteurRole() );
        publicationResponse.setCategorie( publication.getCategorie() );
        List<String> list = publication.getMedias();
        if ( list != null ) {
            publicationResponse.setMedias( new ArrayList<String>( list ) );
        }
        publicationResponse.setLocalisation( publication.getLocalisation() );
        publicationResponse.setStatut( publication.getStatut() );
        publicationResponse.setDateExpiration( publication.getDateExpiration() );
        publicationResponse.setCreatedAt( publication.getCreatedAt() );
        publicationResponse.setUpdatedAt( publication.getUpdatedAt() );
        publicationResponse.setNbSignalements( publication.getNbSignalements() );
        publicationResponse.setEstSignalee( publication.getEstSignalee() );

        return publicationResponse;
    }

    @Override
    public Publication toEntity(PublicationRequest request) {
        if ( request == null ) {
            return null;
        }

        Publication publication = new Publication();

        publication.setType( request.getType() );
        publication.setTitre( request.getTitre() );
        publication.setDescription( request.getDescription() );
        publication.setCategorie( request.getCategorie() );
        List<String> list = request.getMedias();
        if ( list != null ) {
            publication.setMedias( new ArrayList<String>( list ) );
        }
        publication.setLocalisation( request.getLocalisation() );
        publication.setDateExpiration( request.getDateExpiration() );

        return publication;
    }

    @Override
    public void updateFromRequest(PublicationRequest request, Publication publication) {
        if ( request == null ) {
            return;
        }

        if ( request.getType() != null ) {
            publication.setType( request.getType() );
        }
        if ( request.getTitre() != null ) {
            publication.setTitre( request.getTitre() );
        }
        if ( request.getDescription() != null ) {
            publication.setDescription( request.getDescription() );
        }
        if ( request.getCategorie() != null ) {
            publication.setCategorie( request.getCategorie() );
        }
        if ( publication.getMedias() != null ) {
            List<String> list = request.getMedias();
            if ( list != null ) {
                publication.getMedias().clear();
                publication.getMedias().addAll( list );
            }
        }
        else {
            List<String> list = request.getMedias();
            if ( list != null ) {
                publication.setMedias( new ArrayList<String>( list ) );
            }
        }
        if ( request.getLocalisation() != null ) {
            publication.setLocalisation( request.getLocalisation() );
        }
        if ( request.getDateExpiration() != null ) {
            publication.setDateExpiration( request.getDateExpiration() );
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
