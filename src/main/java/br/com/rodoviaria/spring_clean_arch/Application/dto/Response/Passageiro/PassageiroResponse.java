package br.com.rodoviaria.spring_clean_arch.app_temp.dto.Response.Passageiro;

import java.util.UUID;

public record PassageiroResponse(
        UUID id,
        String nome,
        String email,
        String cpf,
        String telefone
) {
}
