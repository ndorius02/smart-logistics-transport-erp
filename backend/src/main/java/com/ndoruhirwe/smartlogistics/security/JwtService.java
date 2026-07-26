package com.ndoruhirwe.smartlogistics.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String AUTHORITIES_CLAIM = "authorities";

    private final SecretKey signingKey;
    private final long expiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.signingKey = buildSigningKey(secret);
        this.expiration = validateExpiration(expiration);
    }

    public String generateToken(Authentication authentication) {
        validateAuthentication(authentication);

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusMillis(expiration);

        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(authentication.getName())
                .claim(AUTHORITIES_CLAIM, authorities)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public List<String> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);

        Object authorities = claims.get(AUTHORITIES_CLAIM);

        if (!(authorities instanceof List<?> authorityList)) {
            return List.of();
        }

        return authorityList.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .toList();
    }

    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ) {
        if (userDetails == null) {
            return false;
        }

        Claims claims = extractAllClaims(token);

        String username = claims.getSubject();
        Date expirationDate = claims.getExpiration();

        return username != null
                && username.equalsIgnoreCase(userDetails.getUsername())
                && expirationDate != null
                && expirationDate.after(new Date())
                && userDetails.isEnabled()
                && userDetails.isAccountNonExpired()
                && userDetails.isAccountNonLocked()
                && userDetails.isCredentialsNonExpired();
    }

    public long getExpiration() {
        return expiration;
    }

    private <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {
        if (claimsResolver == null) {
            throw new IllegalArgumentException(
                    "Claims resolver cannot be null"
            );
        }

        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        validateToken(token);

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey buildSigningKey(String secret) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException(
                    "JWT secret cannot be null or blank"
            );
        }

        try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException(
                    "JWT secret must be a valid and sufficiently strong Base64 key",
                    exception
            );
        }
    }

    private long validateExpiration(long expiration) {
        if (expiration <= 0) {
            throw new IllegalArgumentException(
                    "JWT expiration must be greater than zero"
            );
        }

        return expiration;
    }

    private void validateAuthentication(
            Authentication authentication
    ) {
        if (authentication == null) {
            throw new IllegalArgumentException(
                    "Authentication cannot be null"
            );
        }

        if (!authentication.isAuthenticated()) {
            throw new IllegalArgumentException(
                    "Authentication must be successful before generating a JWT"
            );
        }

        if (authentication.getName() == null
                || authentication.getName().isBlank()) {
            throw new IllegalArgumentException(
                    "Authenticated username cannot be null or blank"
            );
        }
    }

    private void validateToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException(
                    "JWT cannot be null or blank"
            );
        }
    }
}
