package br.com.rodoviaria.spring_clean_arch.application.dto.request.viagem;


import java.time.LocalDateTime;

public record BuscarViagensDisponiveisRequest(
        LocalDateTime data,
        String origem,
        String destino
) {
}
