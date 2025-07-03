package br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro;

import java.util.UUID;

public record PassageiroPorViagemResponse(
        UUID passageiroId,
        UUID viagemId,
        String nome,
        String telefone,
        String origem,
        String destino
) {
}