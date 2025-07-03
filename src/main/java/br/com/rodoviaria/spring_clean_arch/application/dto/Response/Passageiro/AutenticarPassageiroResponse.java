package br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro;

import java.util.UUID;

public record AutenticarPassageiroResponse(
        UUID passageiroId,
        String nome,
        String email,
        String token
        ) {
}
