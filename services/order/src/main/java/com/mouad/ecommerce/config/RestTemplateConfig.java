package com.mouad.ecommerce.config;

import com.mouad.ecommerce.interceptor.RestTemplateInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Autowired
    private RestTemplateInterceptor restTemplateInterceptor;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Ajouter l'intercepteur pour gérer les Tokens JWT
        restTemplate.setInterceptors(List.of(restTemplateInterceptor));
        return restTemplate;
    }
}
