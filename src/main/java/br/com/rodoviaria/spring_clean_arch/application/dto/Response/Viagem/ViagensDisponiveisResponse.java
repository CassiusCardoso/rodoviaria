package br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;

import java.time.LocalDateTime;

public record ViagensDisponiveisResponse(
        LocalDateTime data,
        String destino,
        String origem,
        OnibusResponse onibus
) {
}
