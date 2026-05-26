package com.universite.plateforme.publication.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(indexName = "publications")
@Setting(settingPath = "elasticsearch/settings.json")
public class PublicationDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(type = FieldType.Text, analyzer = "french")
    private String titre;

    @Field(type = FieldType.Text, analyzer = "french")
    private String description;

    @Field(type = FieldType.Keyword)
    private String auteurId;

    @Field(type = FieldType.Keyword)
    private String auteurNom;

    @Field(type = FieldType.Keyword)
    private String categorie;

    @Field(type = FieldType.Keyword)
    private String localisation;

    @Field(type = FieldType.Keyword)
    private String statut;

    @Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDate dateExpiration;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createdAt;

    public PublicationDocument() {}

    public String getId()                  { return id; }
    public String getType()                { return type; }
    public String getTitre()               { return titre; }
    public String getDescription()         { return description; }
    public String getAuteurId()            { return auteurId; }
    public String getAuteurNom()           { return auteurNom; }
    public String getCategorie()           { return categorie; }
    public String getLocalisation()        { return localisation; }
    public String getStatut()              { return statut; }
    public LocalDate getDateExpiration()   { return dateExpiration; }
    public LocalDateTime getCreatedAt()    { return createdAt; }

    public void setId(String id)                        { this.id = id; }
    public void setType(String type)                    { this.type = type; }
    public void setTitre(String titre)                  { this.titre = titre; }
    public void setDescription(String description)      { this.description = description; }
    public void setAuteurId(String auteurId)            { this.auteurId = auteurId; }
    public void setAuteurNom(String auteurNom)          { this.auteurNom = auteurNom; }
    public void setCategorie(String categorie)          { this.categorie = categorie; }
    public void setLocalisation(String localisation)    { this.localisation = localisation; }
    public void setStatut(String statut)                { this.statut = statut; }
    public void setDateExpiration(LocalDate d)          { this.dateExpiration = d; }
    public void setCreatedAt(LocalDateTime createdAt)   { this.createdAt = createdAt; }
}
