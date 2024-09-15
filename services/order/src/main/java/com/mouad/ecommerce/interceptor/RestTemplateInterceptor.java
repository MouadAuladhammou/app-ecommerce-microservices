package com.mouad.ecommerce.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;


import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private final String headerName = "Authorization"; // Nom de l'en-tête

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        // Récupérer le jeton JWT depuis le SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthToken = (JwtAuthenticationToken) authentication;
            String jwtToken = jwtAuthToken.getToken().getTokenValue();
            // Ajouter l'en-tête Authorization avec le token JWT
            request.getHeaders().set(headerName, "Bearer " + jwtToken);
        }

        return execution.execute(request, body);
    }
}
