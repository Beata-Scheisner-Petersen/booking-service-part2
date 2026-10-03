package service.booking.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
public class JwtService {
    final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${JWT_SECRET}")
    private String SECRET_KEY;

    public Long extractUserId(String token) {
        String subject = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        logger.info("Extracting userId from token");

        return Long.valueOf(subject);
    }

    public Boolean isTokenValid(String token) {
        try {
            extractUserId(token);
            System.err.println("JWT Token valid: ");
            logger.info("JWT-token is valid.");
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.err.println("JWT Token has expired: " + e.getMessage());
            logger.error("JWT-token is expired");
            return false;
        } catch (io.jsonwebtoken.JwtException e) {
            System.err.println("Invalid JWT Token: " + e.getMessage());
            logger.error("JWT-token is invalid.");
            return false;
        }
    }

    private SecretKey getSignInKey() {
        byte[] bytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }
}
