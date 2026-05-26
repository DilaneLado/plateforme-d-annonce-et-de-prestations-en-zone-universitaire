package com.universite.plateforme.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // ✅ Mode DEV : utilisateur de test injecté automatiquement
        UserPrincipal testUser = UserPrincipal.builder()
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
                .email("test@univ-dschang.cm")
                .nom("Utilisateur Test")
                .role("ETUDIANT")
                .password("")
                .build();

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        testUser, null, testUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // ======= CODE JWT ORIGINAL (réactiver en prod) =======
        // try {
        //     String token = extractToken(request);
        //     if (token != null) {
        //         Claims claims = Jwts.parser()...
        //         UserPrincipal principal = UserPrincipal.builder()...
        //         UsernamePasswordAuthenticationToken auth = ...
        //         SecurityContextHolder.getContext().setAuthentication(auth);
        //     }
        // } catch (Exception e) {
        //     log.warn("JWT invalide : {}", e.getMessage());
        // }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
