package com.esameIts.CharmApp.security;

import com.esameIts.CharmApp.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

@Service
public class JwtService {
  private final SecretKey key;
  private final long accessMillis;

  public JwtService(@Value("${jwt.secret}") String secret,
                    @Value("${jwt.access-token-minutes}") long accessMinutes) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes()); // >=32 chars
    this.accessMillis = accessMinutes * 60 * 1000;
  }

  public String generateAccessToken(User user) {
    Instant now = Instant.now();
    return Jwts.builder()
        .subject(user.getEmail())
        .claim("roles", user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList()))
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusMillis(accessMillis)))
        .signWith(key)
        .compact();
  }

  public boolean validate(String token) {
    try { Jwts.parser().verifyWith(key).build().parseSignedClaims(token); return true; }
    catch (Exception e) { return false; }
  }

  public String getUsername(String token) {
    Claims c = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    return c.getSubject();
  }
}
