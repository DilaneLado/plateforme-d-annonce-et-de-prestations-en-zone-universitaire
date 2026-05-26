package com.universite.plateforme.publication.repository;

import com.universite.plateforme.common.enums.StatutPublication;
import com.universite.plateforme.common.enums.TypePublication;
import com.universite.plateforme.publication.entity.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, UUID> {

    Page<Publication> findByStatut(StatutPublication statut, Pageable pageable);

    Page<Publication> findByAuteurId(UUID auteurId, Pageable pageable);

    Page<Publication> findByTypeAndStatut(TypePublication type, StatutPublication statut, Pageable pageable);

    Page<Publication> findByStatutAndCategorie(StatutPublication statut, String categorie, Pageable pageable);

    Page<Publication> findByStatutAndLocalisation(StatutPublication statut, String localisation, Pageable pageable);

    Page<Publication> findByEstSignaleeTrue(Pageable pageable);

    @Query("SELECT p FROM Publication p WHERE p.statut = :statut " +
           "AND (:type IS NULL OR p.type = :type) " +
           "AND (:categorie IS NULL OR p.categorie = :categorie) " +
           "AND (:localisation IS NULL OR LOWER(p.localisation) LIKE LOWER(CONCAT('%', :localisation, '%')))")
    Page<Publication> findWithFilters(
            @Param("statut") StatutPublication statut,
            @Param("type") TypePublication type,
            @Param("categorie") String categorie,
            @Param("localisation") String localisation,
            Pageable pageable);

    @Modifying
    @Query("UPDATE Publication p SET p.nbSignalements = p.nbSignalements + 1, p.estSignalee = true WHERE p.id = :id")
    void incrementerSignalement(@Param("id") UUID id);

    long countByStatut(StatutPublication statut);

    long countByType(TypePublication type);

    @Query("SELECT p.type, COUNT(p) FROM Publication p GROUP BY p.type")
    List<Object[]> countByTypeGrouped();
}
