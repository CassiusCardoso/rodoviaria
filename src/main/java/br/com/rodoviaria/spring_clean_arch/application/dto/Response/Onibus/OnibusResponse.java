package br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus;

import java.time.LocalDateTime;
import java.util.UUID;

public record OnibusResponse(
        UUID id,
        String placa,
        String modelo,
        int capacidade,
        Boolean ativo,
        LocalDateTime criadoEm // 2. Adicione o novo campo
) {
}
