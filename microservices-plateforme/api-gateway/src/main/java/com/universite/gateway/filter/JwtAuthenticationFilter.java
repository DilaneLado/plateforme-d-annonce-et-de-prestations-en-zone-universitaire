package com.universite.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth",
            "/api/v1/publications",
            "/api/v1/recherche",
            "/actuator",
            "/swagger-ui",
            "/api-docs"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Laisser passer /api/auth sans token (login, register)
        if (path.startsWith("/api/auth")) {
            return chain.filter(exchange);
        }

        // Laisser passer les docs et actuator
        if (path.contains("/swagger-ui") || path.contains("/api-docs") || path.contains("/actuator")) {
            return chain.filter(exchange);
        }

        // Laisser passer les GET publics (consultation sans compte)
        if (isPublicPath(path) && exchange.getRequest().getMethod().name().equals("GET")) {
            return chain.filter(exchange);
        }

        // Toute autre requête doit avoir un token valide
        String token = extractToken(exchange.getRequest().getHeaders());
        if (token == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(r -> r
                            .header("X-User-Id",    claims.getSubject())
                            .header("X-User-Email", claims.get("email", String.class))
                            .header("X-User-Nom",   claims.get("nom",   String.class))
                            .header("X-User-Role",  claims.get("role",  String.class))
                    )
                    .build();
            return chain.filter(modifiedExchange);

        } catch (Exception e) {
            log.warn("JWT invalide : {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private String extractToken(HttpHeaders headers) {
        String bearer = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}