package com.dschang.plateforme.notification.repository;

import com.dschang.plateforme.notification.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    Page<Notification> findByDestinataireIdOrderByCreatedAtDesc(String destinataireId, Pageable pageable);
}