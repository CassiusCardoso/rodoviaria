package br.com.rodoviaria.spring_clean_arch.application.dto.request.viagem;

import java.time.LocalDateTime;
import java.util.UUID;

public record ViagemRequest(
        LocalDateTime dataPartida,
        UUID linhaId,
        UUID onibusId
) {
}
