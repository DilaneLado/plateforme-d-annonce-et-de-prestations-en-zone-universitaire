package com.dschang.plateforme.messagerie.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;

    // La clé secrète est lue depuis application.yml (doit être identique à celle du service Compte)
    public JwtTokenProvider(@Value("${app.jwt.secret}") String secret) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
    try {
        Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        return true;
    } catch (Exception e) {
        System.out.println(">>> Erreur validation JWT : " + e.getClass().getSimpleName() + " - " + e.getMessage());
        return false;
    }
}

    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return UUID.fromString(claims.getSubject());
    }
}