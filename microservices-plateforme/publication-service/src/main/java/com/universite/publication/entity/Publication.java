package com.universite.publication.entity;

import com.universite.publication.common.enums.StatutPublication;
import com.universite.publication.common.enums.TypePublication;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "publications", indexes = {
        @Index(name = "idx_pub_type",    columnList = "type"),
        @Index(name = "idx_pub_statut",  columnList = "statut"),
        @Index(name = "idx_pub_auteur",  columnList = "auteur_id"),
        @Index(name = "idx_pub_created", columnList = "created_at")
})
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypePublication type;

    @Column(nullable = false, length = 100)
    private String titre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "auteur_id", nullable = false)
    private UUID auteurId;

    @Column(name = "auteur_nom")
    private String auteurNom;

    @Column(name = "auteur_role")
    private String auteurRole;

    @Column(length = 100)
    private String categorie;

    @ElementCollection
    @CollectionTable(name = "publication_medias", joinColumns = @JoinColumn(name = "publication_id"))
    @Column(name = "media_url")
    private List<String> medias = new ArrayList<>();

    @Column(length = 150)
    private String localisation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPublication statut;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "nb_signalements")
    private Integer nbSignalements;

    @Column(name = "est_signalee")
    private Boolean estSignalee;

    // ===== Constructeurs =====

    public Publication() {}

    // ===== Getters =====

    public UUID getId()                  { return id; }
    public TypePublication getType()     { return type; }
    public String getTitre()             { return titre; }
    public String getDescription()       { return description; }
    public UUID getAuteurId()            { return auteurId; }
    public String getAuteurNom()         { return auteurNom; }
    public String getAuteurRole()        { return auteurRole; }
    public String getCategorie()         { return categorie; }
    public List<String> getMedias()      { return medias; }
    public String getLocalisation()      { return localisation; }
    public StatutPublication getStatut() { return statut; }
    public LocalDate getDateExpiration() { return dateExpiration; }
    public LocalDateTime getCreatedAt()  { return createdAt; }
    public LocalDateTime getUpdatedAt()  { return updatedAt; }
    public Integer getNbSignalements()   { return nbSignalements; }
    public Boolean getEstSignalee()      { return estSignalee; }

    // ===== Setters =====

    public void setId(UUID id)                          { this.id = id; }
    public void setType(TypePublication type)           { this.type = type; }
    public void setTitre(String titre)                  { this.titre = titre; }
    public void setDescription(String description)      { this.description = description; }
    public void setAuteurId(UUID auteurId)              { this.auteurId = auteurId; }
    public void setAuteurNom(String auteurNom)          { this.auteurNom = auteurNom; }
    public void setAuteurRole(String auteurRole)        { this.auteurRole = auteurRole; }
    public void setCategorie(String categorie)          { this.categorie = categorie; }
    public void setMedias(List<String> medias)          { this.medias = medias; }
    public void setLocalisation(String localisation)    { this.localisation = localisation; }
    public void setStatut(StatutPublication statut)     { this.statut = statut; }
    public void setDateExpiration(LocalDate d)          { this.dateExpiration = d; }
    public void setCreatedAt(LocalDateTime createdAt)   { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt)   { this.updatedAt = updatedAt; }
    public void setNbSignalements(Integer nb)           { this.nbSignalements = nb; }
    public void setEstSignalee(Boolean estSignalee)     { this.estSignalee = estSignalee; }

    // ===== Valeurs par défaut JPA =====

    @PrePersist
    public void prePersist() {
        if (statut == null)         statut = StatutPublication.EN_MODERATION;
        if (nbSignalements == null) nbSignalements = 0;
        if (estSignalee == null)    estSignalee = false;
        if (medias == null)         medias = new ArrayList<>();
    }
}
