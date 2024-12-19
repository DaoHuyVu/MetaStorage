package com.atbmtt.l01.MetaStorage.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtils {
    @Value("${jwtSecret}")
    private String jwtSecret;
    @Value("${SERVER_HOST}")
    private String serverHost;
    public String createJwt(String subject){
        Date now = new Date();
        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .issuer(serverHost)
                .subject(subject)
                .audience().add(serverHost)
                .and()
//                .expiration(now + )
                .issuedAt(now)
                .signWith(secretKey())
                .compact();
    }
    private SecretKey secretKey(){
        byte[] key = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(key);
    }
    public void verifyJwt(String accessToken) throws JwtException {
        Jwts.parser().verifyWith(secretKey()).build().parseSignedClaims(accessToken);
    }
    public String getSubjectFromJwt(String token){
        return Jwts
                .parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
