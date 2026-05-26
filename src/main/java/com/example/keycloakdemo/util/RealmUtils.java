package com.example.keycloakdemo.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RealmUtils {

    private RealmUtils() {
    }

    public static String getRealmFromRequest() {

        ServletRequestAttributes attributes =
                (ServletRequestAttributes)
                        RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new RuntimeException(
                    "No request context available"
            );
        }

        HttpServletRequest request =
                attributes.getRequest();

        String realm =
                request.getHeader("x-kxh-realm");

        if (realm == null || realm.trim().isEmpty()) {

            /**
             * fallback demo
             */
            return "demo";
        }

        return realm.trim();
    }
}