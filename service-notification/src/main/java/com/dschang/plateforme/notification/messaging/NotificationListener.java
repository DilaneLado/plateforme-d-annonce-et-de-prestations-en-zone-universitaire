package com.dschang.plateforme.notification.messaging;

import com.dschang.plateforme.notification.model.TypeNotification;
import com.dschang.plateforme.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(NotificationListener.class);
    private final NotificationService notificationService;

    public NotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "notification.message.queue")
    public void handleNouveauMessage(NouveauMessageEvent event) {
        logger.info("Réception NouveauMessage pour destinataire {}", event.getDestinataireId());
        notificationService.creerNotification(
                event.getDestinataireId(),
                TypeNotification.MESSAGE,
                "Nouveau message",
                "Vous avez reçu un nouveau message : " + event.getContenu(),
                "/conversations/" + event.getConversationId(),
                false, // pas d'email pour un message instantané par défaut
                null
        );
    }

    @RabbitListener(queues = "notification.publication.queue")
    public void handlePublicationCree(PublicationCreeEvent event) {
        logger.info("Réception PublicationCree pour auteur {}", event.getAuteurId());
        // Par exemple, notifier l'auteur que son annonce est en ligne
        notificationService.creerNotification(
                event.getAuteurId(),
                TypeNotification.NOUVELLE_ANNONCE,
                "Annonce publiée",
                "Votre annonce '" + event.getTitre() + "' est maintenant en ligne.",
                "/publications/" + event.getId(),
                false,
                null
        );
    }

    @RabbitListener(queues = "notification.compte.queue")
    public void handleCompteValide(CompteValideEvent event) {
        logger.info("Réception CompteValide pour utilisateur {}", event.getUserId());
        // Notification in-app + email
        notificationService.creerNotification(
                event.getUserId(),
                TypeNotification.VALIDATION_COMPTE,
                "Compte validé",
                "Votre compte a été validé avec succès. Bienvenue !",
                null,
                true,
                event.getEmail()
        );
    }
}