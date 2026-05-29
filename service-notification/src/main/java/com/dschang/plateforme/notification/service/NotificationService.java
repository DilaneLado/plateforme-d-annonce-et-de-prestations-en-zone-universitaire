package com.dschang.plateforme.notification.service;

import com.dschang.plateforme.notification.dto.NotificationResponse;
import com.dschang.plateforme.notification.model.Notification;
import com.dschang.plateforme.notification.model.TypeNotification;
import com.dschang.plateforme.notification.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository repository;
    private final JavaMailSender mailSender;  // sera optionnel si pas configuré

    public NotificationService(NotificationRepository repository,
                               @Autowired(required = false) JavaMailSender mailSender) {
        this.repository = repository;
        this.mailSender = mailSender;
    }

    // Créer une notification in-app et éventuellement envoyer un email
    public NotificationResponse creerNotification(String destinataireId, TypeNotification type,
                                                  String titre, String contenu, String lien,
                                                  boolean envoyerEmail, String emailDestinataire) {
        Notification notif = new Notification();
        notif.setDestinataireId(destinataireId);
        notif.setType(type);
        notif.setTitre(titre);
        notif.setContenu(contenu);
        notif.setLien(lien);
        notif = repository.save(notif);

        // Envoyer un email si demandé et si le mailSender est configuré
        if (envoyerEmail && emailDestinataire != null && mailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(emailDestinataire);
                message.setSubject(titre);
                message.setText(contenu);
                mailSender.send(message);
                logger.info("Email envoyé à {}", emailDestinataire);
            } catch (Exception e) {
                logger.error("Erreur lors de l'envoi de l'email", e);
            }
        }

        return convertToResponse(notif);
    }

    // Récupérer les notifications d'un utilisateur
    public Page<NotificationResponse> getNotifications(String destinataireId, int page, int taille) {
        Pageable pageable = PageRequest.of(page, taille);
        Page<Notification> notifs = repository.findByDestinataireIdOrderByCreatedAtDesc(destinataireId, pageable);
        return notifs.map(this::convertToResponse);
    }

    // Marquer une notification comme lue
    public NotificationResponse marquerLue(String id) {
        Notification notif = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée"));
        notif.setLue(true);
        notif = repository.save(notif);
        return convertToResponse(notif);
    }

    private NotificationResponse convertToResponse(Notification notif) {
        NotificationResponse dto = new NotificationResponse();
        dto.setId(notif.getId());
        dto.setDestinataireId(notif.getDestinataireId());
        dto.setType(notif.getType());
        dto.setTitre(notif.getTitre());
        dto.setContenu(notif.getContenu());
        dto.setLien(notif.getLien());
        dto.setLue(notif.isLue());
        dto.setCreatedAt(notif.getCreatedAt());
        return dto;
    }
}