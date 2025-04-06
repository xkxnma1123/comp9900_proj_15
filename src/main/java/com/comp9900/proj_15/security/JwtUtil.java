package com.comp9900.proj_15.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    // Provide default value if configuration property is not injected
    @Value("${jwt.secret:defaultSecretKeyWhichIsLongEnoughForHS256Signature}")
    private String secret;

    // Default expiration time set to 24 hours (in seconds)
    @Value("${jwt.expiration:86400}")
    private Long expiration;

    // Generate token method, suitable for generating token from Map
    public String generateToken(Map<String, Object> userMap) {
        // Security check and default value setting
        Long userId = null;
        if (userMap.get("id") != null) {
            if (userMap.get("id") instanceof Number) {
                userId = ((Number) userMap.get("id")).longValue();
            } else {
                try {
                    userId = Long.parseLong(userMap.get("id").toString());
                } catch (Exception e) {
                    // Conversion failed, keep userId as null
                }
            }
        }

        String email = userMap.get("email") != null ? userMap.get("email").toString() : "";
        String userType = userMap.get("user_type") != null ? userMap.get("user_type").toString() : "normal";

        // Generate token
        return Jwts.builder()
                .setSubject(userId != null ? userId.toString() : "unknown")
                .claim("email", email)
                .claim("role", userType)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}