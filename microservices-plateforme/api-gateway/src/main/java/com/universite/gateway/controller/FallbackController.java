package com.universite.gateway.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FallbackController {

    @RequestMapping("/fallback/publication")
    public ResponseEntity<Map<String,String>> publicationFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "error", "Service publications temporairement indisponible",
                        "message", "Veuillez réessayer dans quelques instants."
                ));
    }

    @RequestMapping("/fallback/recherche")
    public ResponseEntity<Map<String,String>> rechercheFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Service recherche temporairement indisponible"));
    }

    @RequestMapping("/fallback/admin")
    public ResponseEntity<Map<String,String>> adminFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Service administration temporairement indisponible"));
    }
}
