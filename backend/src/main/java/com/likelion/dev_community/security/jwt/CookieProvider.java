package com.likelion.dev_community.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CookieProvider {

    @Value("${cookie.secure}")
    private boolean cookieSecure;

    public ResponseCookie createCookie(String name, String value, Duration expiration) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Strict")
                .path("/")
                .maxAge(expiration)
                .build();
    }

    public ResponseCookie clearCookie(String name) {
        return createCookie(name, null, Duration.ZERO);
    }
}
