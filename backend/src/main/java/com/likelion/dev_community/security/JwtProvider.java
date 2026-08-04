package com.likelion.dev_community.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {

    private final SecretKey accessKey;
    private final long accessTokenExpirationMs;
    private final SecretKey refreshKey;
    private final long refreshTokenExpirationMs;

    public JwtProvider(@Value("${jwt.secret}") String secretKey,
                       @Value("${jwt.expirationMs}") long expirationMs,
                       @Value("${jwt.refreshSecret}") String refreshSecretKey,
                       @Value("${jwt.refreshExpirationMs}") long refreshExpirationMs) {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.accessTokenExpirationMs = expirationMs;
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretKey));
        this.refreshTokenExpirationMs = refreshExpirationMs;
    }

    public String createAccessToken(Long userId, String username,String nickname, List<String> roles){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationMs);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("username", username)
                .claim("nickname",nickname)
                .claim("roles",roles)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(accessKey)
                .compact();
    }

    public String createRefreshToken(Long userId){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationMs);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(refreshKey)
                .compact();
    }

    // accessToken 파싱
    public Claims parseAccessToken(String token){
        return Jwts.parser()
                .verifyWith(accessKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // refreshToken 파싱
    public Claims parseRefreshToken(String token){
        return Jwts.parser()
                .verifyWith(refreshKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long getRefreshTokenExpirationMs() {
        return refreshTokenExpirationMs;
    }
}
