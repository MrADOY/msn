package com.msn.chat.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * 
 * @author aurelienpietrzak
 * Swagger permet la description d'une API d'un service rest
 * accessible sur l'adresse http://localhost:port/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(paths())
                .build();
    }

    // Description de l'API
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API de gestion des envoies de message")
                .description("Cette page contient toutes les descriptions des api du service chat.")
                .version("1.0-SNAPSHOT")
                .build();
    }

    // Predicat pour la selection des api a decrire
    private Predicate<String> paths() {
    		// On accepte tout sauf les api pour les erreurs 
        return Predicates.and(
        	PathSelectors.regex("/.*"), 
        	Predicates.not(PathSelectors.regex("/error.*"))
        );
    }
}