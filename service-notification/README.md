# Service Notifications – Plateforme de Prestations Universitaires

Service de gestion des notifications (in‑app, email, push mobiles) de la plateforme.

## Technologies

- **Java 17**, **Spring Boot 3.2.x**
- **Spring Data MongoDB** – stockage des notifications en base documentaire
- **Spring for RabbitMQ** – écoute des événements métier (message reçu, annonce créée, compte validé)
- **Spring Mail** – envoi d'emails via SMTP (optionnel)
- **MongoDB** – base de données NoSQL pour les notifications
- **Docker** – conteneurisation des services (MongoDB, RabbitMQ)

## Architecture

Le service suit une architecture en couches :

| Couche | Dossier | Rôle |
|--------|---------|------|
| **Controller** | `controller/` | Endpoints REST (`/api/notifications`) |
| **Service** | `service/` | Logique métier (création, récupération, marquage lu, envoi email) |
| **Repository** | `repository/` | Interface MongoDB pour les notifications |
| **Model** | `model/` | Document `Notification` et enum `TypeNotification` |
| **DTO** | `dto/` | Objets de transfert (`NotificationResponse`) |
| **Config** | `config/` | Configuration RabbitMQ (queues, bindings, convertisseur JSON), OpenAPI |
| **Exception** | `exception/` | Gestion globale des erreurs |
| **Messaging** | `messaging/` | Listener RabbitMQ (`NotificationListener`) et classes d’événements |

## Endpoints REST

| Méthode | Chemin | Description | Authentification (prévue) |
|---------|--------|-------------|---------------------------|
| GET | `/api/notifications` | Liste les notifications d'un utilisateur (pag.) | JWT |
| PUT | `/api/notifications/{id}/lue` | Marque une notification comme lue | JWT |

### Paramètres de recherche

- `destinataireId` (String, requis) : UUID du destinataire
- `page` (int, défaut 0)
- `taille` (int, défaut 10)

## Flux de notification

1. D'autres services émettent des événements RabbitMQ.
2. Le listener consomme ces événements et crée une notification in‑app (MongoDB).
3. Pour certains types (ex. validation de compte), un email est également envoyé.

**Événements écoutés :**

| Événement | Routing Key | Queue | Action |
|-----------|-------------|-------|--------|
| `NouveauMessage` | `message.nouveau` | `notification.message.queue` | Notification in‑app "Nouveau message" |
| `PublicationCree` | `publication.cree` | `notification.publication.queue` | Notification in‑app "Annonce publiée" |
| `CompteValide` | `compte.valide` | `notification.compte.queue` | Notification in‑app + email de bienvenue |

## Lancer localement

### Prérequis
- Java 17
- Docker (pour MongoDB et RabbitMQ)

### 1. Démarrer les conteneurs
```bash
docker run -d --name mongodb -p 27017:27017 mongo:7.0
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.12-management




2. Configuration
Ajuster src/main/resources/application.yml si nécessaire (ports MongoDB, RabbitMQ, paramètres SMTP pour l’email).

3. Lancer le service
bash
./mvnw spring-boot:run
Le service sera accessible sur http://localhost:8083.

4. Tester
Publier des événements de test via l’interface RabbitMQ (http://localhost:15672).

Récupérer les notifications :
curl "http://localhost:8083/api/notifications?destinataireId=user-2"

Marquer lue :
curl -Method PUT "http://localhost:8083/api/notifications/{id}/lue"

Documentation OpenAPI
L’interface Swagger UI est disponible au démarrage :
http://localhost:8083/swagger-ui.html

Licence
Projet interne – Plateforme de Prestations Universitaires