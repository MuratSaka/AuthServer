package com.project.auth.security;

import com.project.auth.exception.ExpiredTokenException;
import com.project.auth.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Murat Saka
 * @created 19/10/2025 - 11:58
 * @project AuthServer
 */
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.expiration}")
    private int expiration; // seconds

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken; // seconds

    @Value("${jwt.secretKey}")
    private String secretKey;

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
    private final TokenRepository tokenRepository;




    /**
     * Secret key'den HMAC-SHA anahtarı oluşturur.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Token'daki tüm claim'leri çeker.
     */
    private Claims extractAllClaims(String token) throws ExpiredTokenException {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith((javax.crypto.SecretKey) getSignInKey())
                    .build()
                    .parseSignedClaims(token);
            return claimsJws.getPayload();
        } catch (JwtException e) {
            logger.warn("JWT parsing hatası: {}", e.getMessage());
            throw new ExpiredTokenException("Geçersiz veya bozuk token: " + e.getMessage());
        }
    }

    /**
     * Token'dan belirli bir claim'i çıkarır.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws ExpiredTokenException {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Token'ın süresi dolmuş mu?
     */
    public boolean isTokenExpired(String token) throws ExpiredTokenException {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * Token'dan email çıkarır.
     */
    public String extractEmail(String token) throws ExpiredTokenException {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Token'ı doğrular: email eşleşiyor mu ve süresi dolmamış mı?
     */
    @SneakyThrows
    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
