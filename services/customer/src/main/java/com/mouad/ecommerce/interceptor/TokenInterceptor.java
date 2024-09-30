package com.mouad.ecommerce.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.Map;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof Jwt) {
                Jwt jwt = (Jwt) principal;

                // Extraire des attributs du token JWT
                String userId = jwt.getSubject(); // Obtenir l'ID de l'utilisateur (sub)
                String email = jwt.getClaimAsString("email");
                Map<String, Object> realmAccess;
                Collection<String> roles;

                realmAccess = jwt.getClaim("realm_access");
                roles = (Collection<String>) realmAccess.get("roles");

                // Injecter les attributs dans la requête
                request.setAttribute("userId", userId);
                request.setAttribute("email", email);
                request.setAttribute("roles",  String.join(", ", roles));
            }
        }

        // Continuer le traitement de la requête
        return true;
    }

}
