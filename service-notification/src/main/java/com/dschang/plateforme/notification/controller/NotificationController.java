package com.dschang.plateforme.notification.controller;

import com.dschang.plateforme.notification.dto.NotificationResponse;
import com.dschang.plateforme.notification.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public Page<NotificationResponse> getNotifications(
            @RequestParam String destinataireId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int taille) {
        return notificationService.getNotifications(destinataireId, page, taille);
    }

    @PutMapping("/{id}/lue")
    public NotificationResponse marquerLue(@PathVariable String id) {
        return notificationService.marquerLue(id);
    }
}