package nesalmanov.ru.authservice.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import nesalmanov.ru.authservice.model.dto.request.UserLoginRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${jwt.private.key}")
    private String privateKey;

    private final int TOKEN_EXPIRATION = 60 * 60 * 1000;

    public String generateWebToken(UserLoginRequest userLoginRequest, HttpServletResponse response) {
        Map<String, Object> claims = new HashMap<>();
        String token = Jwts.builder()
                .claims()
                .add(claims)
                .subject(userLoginRequest.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()  + TOKEN_EXPIRATION))
                .and()
                .signWith(getKey())
                .compact();

        Cookie cookie = new Cookie("jwt", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return "Token in cookie";
    }

    public String generateMobileToken(UserLoginRequest userLoginRequest) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userLoginRequest.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()  + TOKEN_EXPIRATION))
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(privateKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
