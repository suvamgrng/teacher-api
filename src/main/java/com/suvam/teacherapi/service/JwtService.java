package com.suvam.teacherapi.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtService {

    @Value("${app.jwt-secret-key}")
    private String secretKey;

    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith((SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Key getSignInKey() {
        byte[] getBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(getBytes);
    }

    public boolean isTokenValid(Claims claims, UserDetails userDetails) {
        final String username = claims.getSubject();

        final boolean isExpired = claims
                .getExpiration()
                .before(new Date());

        return (username.equals(userDetails.getUsername()) && !isExpired);
    }

    public String generateToken(
            Map<String, Object> claims,
            UserDetails userDetails
    ) {
        final long currentTime = System.currentTimeMillis();
        final long expTime = currentTime + (1000L * 60 * 60 *24);

        return Jwts
                .builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(currentTime))
                .expiration(new Date(expTime))
                .signWith(getSignInKey())
                .compact();
    }
}
