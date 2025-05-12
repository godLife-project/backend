package com.godLife.project.jwt;

import com.godLife.project.exception.UnauthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

  public String extractJwt(final StompHeaderAccessor accessor) {
    return accessor.getFirstNativeHeader("Authorization");
  }

  public void validateToken(final String token) {
    try {
      Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    } catch (SecurityException | MalformedJwtException | SignatureException | WeakKeyException e) {
      throw UnauthorizedException.of(e.getClass().getName(), "잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {
      throw UnauthorizedException.of(e.getClass().getName(), "만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException e) {
      throw UnauthorizedException.of(e.getClass().getName(), "지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      throw UnauthorizedException.of(e.getClass().getName(), "JWT 토큰이 잘못되었습니다.");
    } catch (JwtException e) {
      throw UnauthorizedException.of(e.getClass().getName(), "JWT 처리 중 오류가 발생했습니다.");
    }
  }

  public Authentication getAuthentication(String token) {
    String username = getUsername(token);
    String role = getRole(token);

    GrantedAuthority authority = new SimpleGrantedAuthority(role);

    return new UsernamePasswordAuthenticationToken(username, null, java.util.Collections.singletonList(authority));
  }
}
