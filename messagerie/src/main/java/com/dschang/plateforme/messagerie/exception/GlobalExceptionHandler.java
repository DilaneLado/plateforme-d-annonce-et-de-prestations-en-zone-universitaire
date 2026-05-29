package com.dschang.plateforme.messagerie.exception;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.annotation.SendToUser;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Exception e) {
        // Log l'erreur
        e.printStackTrace();
        return "Erreur : " + e.getMessage();
    }
}