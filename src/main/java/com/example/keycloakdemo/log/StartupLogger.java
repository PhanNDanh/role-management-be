package com.example.keycloakdemo.log;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupLogger {

    @EventListener(ApplicationReadyEvent.class)
    public void printLinks() {

        System.out.println("""
                                
                ====================================================
                KEYCLOAK DEMO STARTED

                Frontend:
                http://localhost:5173

                Backend:
                http://localhost:8081

                Swagger:
                http://localhost:8081/swagger-ui/index.html
                                
                Keycloak:
                http://localhost:8180
                
                Realm:
                http://localhost:8180/realms/demo

                OpenAPI:
                http://localhost:8081/v3/api-docs

                OpenID:
                http://localhost:8180/realms/demo/.well-known/openid-configuration

                ====================================================

                """);
    }
}
