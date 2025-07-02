package br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro;

import java.util.UUID;

public record PassageiroResponse(
        UUID id,
        String nome,
        String email,
        String cpf,
        String telefone
) {
}
