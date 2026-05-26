# Plateforme Universitaire - Backend

Projet Spring Boot pour la Plateforme d'Annonces et de Prestations - Université de Dschang.

## Stack technique
- **Java 17** + **Spring Boot 3.2.5**
- **MySQL 8** (données principales)
- **Elasticsearch 8** (recherche full-text)
- **JWT** (authentification stateless)
- **Swagger / OpenAPI 3** (documentation)

## Modules implémentés
- `publication` — CRUD complet des annonces (5 types)
- `recherche` — Recherche full-text + filtres via Elasticsearch
- `administration` — Tableau de bord, modération, statistiques

## Lancer l'infrastructure

```bash
docker-compose up -d
```

## Lancer l'application

```bash
./mvnw spring-boot:run
```

## Documentation API
- Swagger UI : http://localhost:8080/swagger-ui.html
- OpenAPI JSON : http://localhost:8080/api-docs

## Variables d'environnement

| Variable | Défaut | Description |
|---|---|---|
| DB_USERNAME | root | MySQL username |
| DB_PASSWORD | root | MySQL password |
| ELASTICSEARCH_URI | http://localhost:9200 | URI Elasticsearch |
| JWT_SECRET | (défaut dev) | Clé secrète JWT (à changer en prod) |

## Endpoints principaux

### Publications (public en lecture)
- `GET  /api/v1/publications` — Lister avec filtres
- `GET  /api/v1/publications/{id}` — Détail
- `POST /api/v1/publications` — Créer (authentifié)
- `PUT  /api/v1/publications/{id}` — Modifier (auteur ou admin)
- `DELETE /api/v1/publications/{id}` — Supprimer (auteur ou admin)
- `POST /api/v1/publications/{id}/signaler` — Signaler

### Recherche (publique)
- `GET /api/v1/recherche?q=&type=&categorie=&localisation=`

### Administration (ADMIN uniquement)
- `GET  /api/v1/admin/statistiques`
- `GET  /api/v1/admin/publications/en-moderation`
- `GET  /api/v1/admin/publications/signalees`
- `PATCH /api/v1/admin/publications/{id}/valider`
- `PATCH /api/v1/admin/publications/{id}/rejeter`
- `PATCH /api/v1/admin/publications/{id}/archiver`
- `DELETE /api/v1/admin/publications/{id}`

## JWT Token attendu
Le token JWT doit contenir les claims suivants :
```json
{
  "sub": "uuid-utilisateur",
  "email": "user@example.com",
  "nom": "Nom Complet",
  "role": "ETUDIANT | ENTREPRISE | PARTICULIER | ADMIN"
}
```
