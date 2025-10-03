package nesalmanov.ru.chatservice.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtKeyConfiguration {

    @Value("${jwt.public.key}")
    private String publicKeyStr;

    @Bean
    public PublicKey publicKey() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}
