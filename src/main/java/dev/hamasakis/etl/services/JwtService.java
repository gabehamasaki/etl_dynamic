package dev.hamasakis.etl.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.hamasakis.etl.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtService {

    @Value("${api.security.token.secret:minha-chave-super-secreta-dev}")
    private String secretKey;

    public String generateToken(User user) {
        try {
            var algorithm = Algorithm.HMAC256(secretKey);

            return JWT.create()
                    .withSubject(user.getEmail())
                    .withIssuer("ETL API")
                    .sign(algorithm);

        } catch (JWTCreationException exception){
            throw new RuntimeException("Generate Token Error", exception);
        }
    }

    public String getSubject(String token) {

        try {
            var algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("ETL API")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("JWT Token Invalid or expired", exception);
        }
    }

    public Instant expiredAt() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
