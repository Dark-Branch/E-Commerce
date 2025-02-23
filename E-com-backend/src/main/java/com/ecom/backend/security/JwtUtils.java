package com.ecom.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        Date date = new Date();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
//        } catch (SignatureException ex) {
//            throw new AuthenticationException("Invalid JWT signature") {};  // Ensures 401
        } catch (ExpiredJwtException ex) {
            throw new AuthenticationException("JWT token is expired") {};  // Ensures 401
        } catch (MalformedJwtException ex) {
            throw new AuthenticationException("Malformed JWT token") {};  // Ensures 401
        } catch (UnsupportedJwtException ex) {
            throw new AuthenticationException("Unsupported JWT token") {};  // Ensures 401
        } catch (IllegalArgumentException ex) {
            throw new AuthenticationException("JWT claims string is empty") {};  // Ensures 401
        }
    }
}
