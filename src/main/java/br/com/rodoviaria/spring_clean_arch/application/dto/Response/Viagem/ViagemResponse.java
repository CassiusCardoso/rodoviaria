package br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record ViagemResponse(
        UUID id,
        LocalDateTime dataPartida,
        LocalDateTime dataHoraChegada,
        String statusViagem,
        String origem,
        String destino
) {
}
