package com.foretell.sportsmeetings.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.foretell.sportsmeetings.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "REST API for SportSMeetingS",
                "This API helps you to work with server.\n" +
                        "Server uses JWT Token, so unauthorized you can use only Authentication Rest Controller methods.\n" +
                        "If you want to use another methods, click on button 'Authorize' and set token in value, " +
                        "which you can get in Authentication Rest Controller methods.\n" +
                        "For example: 'eyJhbGciOiJIUzUxMiJ9'.",
                "1.0",
                null,
                new Contact("Daniil Karanov", "https://daniilkaranov.ru/", "daniilkaranov@yandex.ru"),
                null,
                null,
                Collections.emptyList());
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("JWT", authorizationScopes));
    }
}