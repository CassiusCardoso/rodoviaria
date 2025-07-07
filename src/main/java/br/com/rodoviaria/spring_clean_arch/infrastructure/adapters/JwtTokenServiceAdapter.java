package br.com.rodoviaria.spring_clean_arch.infrastructure.adapters;

import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.TokenServicePort;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service // Anotação para o Spring gerenciá-lo como um serviço
public class JwtTokenServiceAdapter implements TokenServicePort {

    @Value("${api.security.token.secret}") // Pega a chave do application.properties
    private String secret;

    @Override
    public String gerarToken(Passageiro passageiro){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("rodoviaria-api")
                .withSubject(passageiro.getEmail().getEmail())
                .withClaim("id", passageiro.getId().toString())
                .withExpiresAt(getExpirationDate())
                .sign(algorithm);
    }

    private Instant getExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
