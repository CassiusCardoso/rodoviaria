package br.com.rodoviaria.spring_clean_arch.infrastructure.adapters;

import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.TokenServicePort;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Administrador;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtTokenServiceAdapter implements TokenServicePort {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenServiceAdapter.class);

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${app.debug.profile}")
    private String debugProfile;

    @Override
    public String gerarToken(Passageiro passageiro) {
        logger.info("Profile em uso: {}", debugProfile);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("rodoviaria-api")
                .withSubject(passageiro.getEmail().getEmail())
                .withClaim("id", passageiro.getId().toString())
                .withExpiresAt(getExpirationDate())
                .sign(algorithm);
    }

    @Override
    public String gerarToken(Administrador administrador) {
        logger.info("Profile em uso: {}", debugProfile);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("rodoviaria-api")
                .withSubject(administrador.getEmail().getEmail())
                .withClaim("id", administrador.getId().toString())
                .withExpiresAt(getExpirationDate())
                .sign(algorithm);
    }

    public String validarToken(String token) {
        try {
            logger.info("Profile em uso: {}", debugProfile);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm)
                    .withIssuer("rodoviaria-api")
                    .build()
                    .verify(token);
            return JWT.decode(token).getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}