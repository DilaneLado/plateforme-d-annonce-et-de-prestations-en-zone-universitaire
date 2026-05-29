# Plateforme Universitaire — Architecture Microservices

Migration du monolithe Spring Boot vers une architecture microservices.  
Université de Dschang.

---

## Architecture

```
Client / Frontend
      │
      ▼
┌─────────────────────────────┐
│       API Gateway :8080      │  ← point d'entrée unique, validation JWT, routage
└─────────────────────────────┘
      │           │           │           │
      ▼           ▼           ▼           ▼
  Publication  Recherche  Administration  (Auth — à venir)
   :8081        :8082        :8083
      │           │
   MySQL      Elasticsearch
   :3306        :9200
      │
  Eureka Registry :8761  ← découverte de services
```

## Services

| Service               | Port | Rôle |
|-----------------------|------|------|
| `eureka-server`       | 8761 | Registre de services (Netflix Eureka) |
| `api-gateway`         | 8080 | Point d'entrée unique, JWT, routage |
| `publication-service` | 8081 | CRUD publications, indexation ES |
| `recherche-service`   | 8082 | Recherche full-text Elasticsearch |
| `administration-service` | 8083 | Back-office admin, délègue via Feign |

## Démarrage rapide

### Avec Docker Compose (recommandé)

```bash
# Construire et démarrer tous les services
docker-compose up --build

# Démarrer en arrière-plan
docker-compose up -d --build

# Voir les logs d'un service
docker-compose logs -f publication-service
```

### Ordre de démarrage manuel (développement)

```
1. MySQL + Elasticsearch  (infrastructure)
2. eureka-server          (registre)
3. publication-service    (données)
4. recherche-service      (lecture ES)
5. administration-service (délègue à publication)
6. api-gateway            (en dernier)
```

### Lancer un service individuellement

```bash
cd publication-service
mvn spring-boot:run
```

## URLs utiles

| URL | Description |
|-----|-------------|
| http://localhost:8080/api/v1/publications | API Publications (via gateway) |
| http://localhost:8080/api/v1/recherche?q=stage | Recherche (via gateway) |
| http://localhost:8080/api/v1/admin/statistiques | Admin (via gateway) |
| http://localhost:8761 | Dashboard Eureka |
| http://localhost:8081/swagger-ui.html | Swagger Publication |
| http://localhost:8082/swagger-ui.html | Swagger Recherche |
| http://localhost:8083/swagger-ui.html | Swagger Administration |

## Communication inter-services

```
administration-service
       │
       │  OpenFeign (load-balanced via Eureka)
       ▼
publication-service  /api/v1/publications/internal/*
```

Les endpoints `/internal/*` du `publication-service` sont réservés à la communication
inter-services. Ils ne passent pas par la Gateway (appel direct service-to-service).

## Authentification

### Mode développement (actuel)

La Gateway injecte automatiquement un utilisateur de test quand aucun token JWT n'est fourni.
Les headers `X-User-Id`, `X-User-Role`, `X-User-Email`, `X-User-Nom` sont propagés aux services.

### Mode production

1. Activer la validation JWT dans `JwtAuthenticationFilter` de l'API Gateway
2. Générer les tokens via un service d'authentification dédié
3. Définir la variable d'environnement `JWT_SECRET`

## Variables d'environnement

| Variable | Défaut | Description |
|----------|--------|-------------|
| `JWT_SECRET` | valeur de dev | Clé secrète JWT (changer en prod !) |
| `ELASTICSEARCH_URI` | `http://localhost:9200` | URL Elasticsearch |
| `DB_PASSWORD` | *(vide)* | Mot de passe MySQL |
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | `http://localhost:8761/eureka/` | URL Eureka |

## Ce qui a changé par rapport au monolithe

| Aspect | Monolithe | Microservices |
|--------|-----------|---------------|
| Déploiement | 1 JAR | 5 JARs indépendants |
| Base de données | 1 MySQL partagé | 1 MySQL dédié (publication) |
| JWT | Validé dans chaque service | Centralisé dans la Gateway |
| Communication admin→publication | Appel de méthode Java direct | HTTP via Feign |
| Scalabilité | Scale tout ou rien | Scale chaque service indépendamment |
| Elasticsearch | Couplé au service publication | Idem (publication écrit, recherche lit) |

## Prochaines étapes recommandées

- [ ] Ajouter un `auth-service` dédié (inscription, connexion, tokens JWT)
- [ ] Ajouter un `config-server` Spring Cloud Config (centraliser les `application.yml`)
- [ ] Mettre en place un message broker (Kafka / RabbitMQ) pour les événements inter-services
- [ ] Ajouter des circuit breakers Resilience4j sur les appels Feign
- [ ] Mettre en place des tests d'intégration avec Testcontainers
