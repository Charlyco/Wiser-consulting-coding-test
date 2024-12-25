package com.wiser.consulting.auth_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.wiser.consulting.auth_service.entity.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtService {
    @Value("${application.security.jwt.secret-key:}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long tokenExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    private String generateToken(
            Map<String, Object> extraClaims,
            User user) {
        return buildToken(extraClaims, user, tokenExpiration);
    }

    public String generateRefreshToken(
            User user) {
        return buildToken(new HashMap<>(), user, refreshTokenExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, User user, long tokenExpiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserName(String authToken) {
        return extractSingleClaim(authToken, Claims::getSubject);
    }

    public <T> T extractSingleClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String authToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(authToken.trim())
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyByte = Decoders.BASE64.decode(secretKey.trim());
        return Keys.hmacShaKeyFor(keyByte);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractSingleClaim(token, Claims::getExpiration).before(new Date());
    }

}
