package com.universite.recherche.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Ce filtre lit les headers propagés par l'API Gateway (X-User-Id, X-User-Role, etc.)
 * et construit l'authentification Spring Security sans revalider le JWT.
 * La validation JWT est centralisée dans la Gateway.
 */
@Slf4j
@Component
public class HeaderAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String userId = request.getHeader("X-User-Id");
        String email  = request.getHeader("X-User-Email");
        String nom    = request.getHeader("X-User-Nom");
        String role   = request.getHeader("X-User-Role");

        if (StringUtils.hasText(userId) && StringUtils.hasText(role)) {
            try {
                UserPrincipal principal = UserPrincipal.builder()
                    .id(UUID.fromString(userId))
                    .email(email != null ? email : "")
                    .nom(nom != null ? nom : "")
                    .role(role)
                    .password("")
                    .build();

                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                log.warn("Impossible de construire le principal depuis les headers : {}", e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }
}
