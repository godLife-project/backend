package com.godLife.project.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

  private final SecretKey secretKey;

  public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
    secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
  }

  public String getUsername(String token) {

    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
  }

  public String getRole(String token) {

    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
  }

  public String getCategory(String token) {

    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
  }

  public Boolean isExpired(String token) {

    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
  }

  public int getIsBanned(String token) {

    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("isBanned", Integer.class);
  }



  public String createJwt(String category, String username, String role, int isBanned, Long expiredMs) {

    return Jwts.builder()
        .claim("category", category)
        .claim("username", username)
        .claim("role", role)
        .claim("isBanned", isBanned)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiredMs))
        .signWith(secretKey)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      // 서명 검증 및 파싱
      Jwts.parser()
              .verifyWith(secretKey)
              .build()
              .parseSignedClaims(token);

      // 만료 여부 검사
      return !isExpired(token);
    } catch (Exception e) {
      // 로그로 남기거나 처리할 수 있음
      System.out.println("JWT 검증 실패: " + e.getMessage());
      return false;
    }
  }
}
