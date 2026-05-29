Voici un `README.md` temporaire pour le service de messagerie, reflétant l’état actuel du projet (sans Eureka). Tu peux le placer à la racine du projet `service-messagerie`.

```markdown
# Service de Messagerie – Plateforme de Prestations Universitaires

Service de chat temps réel basé sur **WebSocket/STOMP** avec authentification JWT, persistance MongoDB et émission d’événements RabbitMQ.

## 📦 État actuel (mai 2026)

- ✅ Envoi / réception de messages via STOMP sur `/app/chat`
- ✅ Gestion des conversations (création, historique)
- ✅ Authentification par token JWT (validation locale)
- ✅ Persistance dans MongoDB (`conversations`, `messages`)
- ✅ Publication d’événements RabbitMQ à chaque nouveau message
- ✅ Indicateur de frappe et marquage « lu »
- ✅ Gestion CORS (autorise les requêtes cross-origin pour les tests)
- ✅ Documentation OpenAPI (Swagger UI disponible)
- ⬜ Intégration à Eureka / Config Server (prochaine étape)
- ⬜ Filtre JWT sur les endpoints REST (pour l’instant seule la partie WebSocket est sécurisée)

## 🚀 Prérequis

- Java 17 (ou 21)
- Maven 3.8+
- Docker (pour MongoDB et RabbitMQ)
- Un token JWT valide pour les tests (généré avec la même clé secrète)

## 🛠️ Lancement rapide

### 1. Démarrer les services Docker
```bash
docker run -d --name mongodb -p 27017:27017 mongo:7.0
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.12-management
```

### 2. Configurer la clé JWT
Dans `src/main/resources/application.yml`, vérifie ou mets à jour la propriété :
```yaml
app:
  jwt:
    secret: votre-cle-secrete-en-base64  # doit être ≥ 256 bits après décodage
```

### 3. Compiler et lancer l'application
```bash
mvn clean compile spring-boot:run
```
L’application démarre sur le port **8083**.

## 🧪 Test avec un client HTML

1. Place le fichier `test-chat.html` (fourni dans `/src/main/resources/static/`) dans ce même dossier.
2. **Génère un token JWT** : utilise la classe temporaire `TokenGeneratorTest` placée dans le package racine, ou l’outil jwt.io avec la même clé et l’algorithme HS256.
3. Ouvre `http://localhost:8083/test-chat.html` dans un navigateur.
4. Colle le token, saisis deux UUIDs factices (celui du `sub` doit correspondre à l’un des participants).
5. Clique sur **Créer / Récupérer conversation**, puis envoie un message.
6. Les messages s’affichent en temps réel.

Tu peux également utiliser l’endpoint REST directement :
```bash
curl -X POST http://localhost:8083/api/conversations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <ton_token>" \
  -d '{"participant1":"<uuid1>","participant2":"<uuid2>"}'
```

## 📡 Endpoints disponibles

### WebSocket (STOMP)
- **Endpoint de connexion** : `ws://localhost:8083/ws-chat` (ou `/ws-chat` via SockJS)
- **Destinations** :
  - `/app/chat` → envoyer un message (payload JSON : `conversationId`, `contenu`)
  - `/app/typing` → indicateur de frappe
  - `/app/read` → marquer un message comme lu
  - `/topic/conversation/{convId}` → recevoir les messages de la conversation
  - `/topic/conversation/{convId}.typing` → recevoir les indicateurs de frappe
  - `/topic/conversation/{convId}.read` → notification de lecture

### REST
| Méthode | Chemin | Description |
|---------|--------|-------------|
| POST | `/api/conversations` | Créer ou récupérer une conversation |
| GET | `/api/conversations/user/{userId}` | Lister les conversations d’un utilisateur |

**Note** : Pour les endpoints REST, l’authentification JWT n’est pas encore exigée (prévu dans une prochaine itération). Tu peux passer l’en-tête `Authorization` mais il n’est pas vérifié actuellement.

## 🧱 Architecture technique

- **Modèle en couches** : `controller`, `service`, `repository`, `model`, `dto`, `config`, `exception`
- **DTOs** : `ChatMessageRequest`, `MessageResponse`, `ConversationResponse`
- **Sécurité WebSocket** : intercepteur STOMP `JwtChannelInterceptor` qui valide le token JWT et extrait l’utilisateur
- **Fournisseur JWT** : `JwtTokenProvider` (validation locale par clé HMAC-SHA256)
- **RabbitMQ** : échange `messagerie-exchange`, routing key `message.nouveau` ; sérialisation JSON via `Jackson2JsonMessageConverter`
- **MongoDB** : base `messagerie`, collections `conversations` et `messages`

## ⚙️ Configuration principale (`application.yml`)
```yaml
server:
  port: 8083

spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/messagerie
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest

app:
  rabbitmq:
    exchange: messagerie-exchange
    routing-key-message: message.nouveau
  jwt:
    secret: 2cF7VZq8e4JxN1vR3mT5yL9wQ0bA6dGhKjMnOpIuYtReWqZaSxCdEfVbGhJkLmNo  # exemple
```

## 🔮 Prochaines étapes

- [ ] Intégration à **Eureka** pour la découverte de services
- [ ] Centralisation de la configuration avec **Config Server**
- [ ] Sécurisation complète des endpoints REST (filtre JWT HTTP)
- [ ] Validation de l’existence des utilisateurs via le service **Compte** (client REST load‑balancé)
- [ ] Ajout de tests unitaires et d’intégration
- [ ] Conteneurisation du service lui‑même (Dockerfile)

## 👨‍💻 Équipe de développement

Projet réalisé dans le cadre de la plateforme de prestations universitaires.
```

Tu peux sauvegarder ce fichier sous `README.md` dans le dossier `service-messagerie`. Il servira de référence à l’équipe pendant la phase d’intégration. Une fois Eureka ajouté, nous le mettrons à jour.