package com.schoolconnect.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtTokenProvider(
            @Value("${app.security.jwt-secret}") String secret,
            @Value("${app.security.jwt-expiration-ms:86400000}") long expirationMs
    ) {
        String paddedSecret = secret.length() >= 32
                ? secret
                : String.format("%-32s", secret).replace(' ', '0');
        this.secretKey = Keys.hmacShaKeyFor(paddedSecret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String generateToken(UserPrincipal principal) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .subject(String.valueOf(principal.id()))
                .claim("fullName", principal.fullName())
                .claim("phone", principal.phone())
                .claim("role", principal.role())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public UserPrincipal parseToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Long userId = Long.parseLong(claims.getSubject());
        String fullName = claims.get("fullName", String.class);
        String phone = claims.get("phone", String.class);
        String role = claims.get("role", String.class);
        return new UserPrincipal(userId, fullName, phone, role);
    }
}

