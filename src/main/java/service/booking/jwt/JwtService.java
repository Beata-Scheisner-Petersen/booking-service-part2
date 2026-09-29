package service.booking.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
public class JwtService {

    @Value("${JWT_SECRET}")
    private String SECRET_KEY;

    public Long extractUserId(String token) {
        String subject = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        return Long.valueOf(subject);
    }

    public Boolean isTokenValid(String token) {
        try {
            extractUserId(token);
            System.err.println("JWT Token valid: ");
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.err.println("JWT Token has expired: " + e.getMessage());
            return false;
        } catch (io.jsonwebtoken.JwtException e) {
            System.err.println("Invalid JWT Token: " + e.getMessage());
            return false;
        }
    }

    private SecretKey getSignInKey() {
        byte[] bytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }
}
