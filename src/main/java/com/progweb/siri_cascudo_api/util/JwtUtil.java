package com.progweb.siri_cascudo_api.util;

import com.progweb.siri_cascudo_api.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Autowired
    private JwtConfig jwtConfig;

    public void JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(String email, List<String> roles, Long id) {
        Key key = Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());

        return Jwts.builder()
                .setSubject(email)
                .claim("id", id.toString())
                .claim("roles", roles) // Adiciona os papéis ao token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationTime()))
                .signWith(key)
                .compact();
    }

    public Claims validateToken(String token) {
        Key key = Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmailFromToken(String token) {
        return validateToken(token).getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        return validateToken(token).get("roles", List.class);
    }

    public Long getIdFromToken(String token) {
        return Long.parseLong(validateToken(token).get("id", String.class));
    }

    public boolean isAdmin(String token) {
        return getRolesFromToken(token).contains("ROLE_ADMIN");
    }
}