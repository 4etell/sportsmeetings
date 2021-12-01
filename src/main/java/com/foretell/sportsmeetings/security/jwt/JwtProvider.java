package com.foretell.sportsmeetings.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.token.secret}")
    private String jwtSecret;

    @Value("${jwt.token.expired}")
    private long validityInMs;

    public String generateToken(String username) {
        Date validity = new Date(new Date().getTime() + validityInMs);
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("invalid token");
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}
