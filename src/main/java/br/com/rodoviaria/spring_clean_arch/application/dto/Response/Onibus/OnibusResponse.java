package br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus;

import java.util.UUID;

public record OnibusResponse(
        UUID id,
        String placa,
        String modelo,
        int capacidade,
        Boolean ativo
        ) {
}
