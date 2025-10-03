package nesalmanov.ru.chatservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.Date;

@Service
public class JwtUtils {

    private final PublicKey publicKey;

    public JwtUtils(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token).getPayload();
        return claims.getSubject();

    }

    private boolean isTokenExpired(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token).getPayload();
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    public boolean validateToken(String token){
        String username = extractUsername(token);
        return username != null && !isTokenExpired(token);
    }
}

