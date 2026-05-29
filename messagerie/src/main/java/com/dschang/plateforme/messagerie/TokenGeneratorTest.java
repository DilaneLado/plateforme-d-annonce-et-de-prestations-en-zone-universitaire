package com.dschang.plateforme.messagerie;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;

public class TokenGeneratorTest {
    public static void main(String[] args) {
        // La même clé que dans application.yml, encodée en Base64
        String secretBase64 = "UIzAfmfs+tBnR2kyNukLczxBsqzxCauFvkAMPFj8DRA0nHqVVWAXlHdc+yhjgqwIUN6rB3enVpjo6waNJwK4kg==";
        byte[] keyBytes = Base64.getDecoder().decode(secretBase64);
        var key = Keys.hmacShaKeyFor(keyBytes);

        String token = Jwts.builder()
                .subject("12345678-1234-1234-1234-123456789abc")
                .claim("role", "ETUDIANT")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 24 heures
                .signWith(key, Jwts.SIG.HS256)   // <-- Algorithme HS256 explicite
                .compact();

        System.out.println("Nouveau token HS256 : " + token);
    }
}