package com.home.sample.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
@Profile("prod")
public class JwtUtils {
    private static final String SECRET_KEY = "secret";

    public String extractUserName(final String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpirationDate(final String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private <T> T extractClaims(final String token, final Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(final String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String generateToken(final UserDetails userDetails) {
        final Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(final Map<String, Object> claims, final String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(final String token, final UserDetails userDetails) {
        return (extractUserName(token).equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(final String token) {
        return extractExpirationDate(token).before(new Date());
    }
}
