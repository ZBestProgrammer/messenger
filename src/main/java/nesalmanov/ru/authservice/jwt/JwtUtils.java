package nesalmanov.ru.authservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import nesalmanov.ru.authservice.model.dto.request.UserLoginRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${jwt.private.key}")
    private PrivateKey privateKey;

    @Value("${jwt.public.key}")
    private PublicKey publicKey;

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
                .signWith(privateKey)
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
                .signWith(publicKey)
                .compact();
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

    public boolean validateToken(String token, UserDetails userDetails){
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

//    private SecretKey getPrivateKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(privateKey);
//        return
//    }

//    private PublicKey getPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
//        byte[] keyBytes = Decoders.BASE64.decode(publicKey);
//        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        return kf.generatePublic(spec);
//    }
}
