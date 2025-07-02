package br.com.rodoviaria.spring_clean_arch.Application.dto.Response.Viagem;

import br.com.rodoviaria.spring_clean_arch.Application.dto.Response.Linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.Application.dto.Response.Onibus.OnibusResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record ViagemResponse(
        UUID id,
        LocalDateTime dataPartida,
        LocalDateTime dataHoraChegada,
        String statusViagem,
        LinhaResponse linha,
        OnibusResponse onibus
) {
}
