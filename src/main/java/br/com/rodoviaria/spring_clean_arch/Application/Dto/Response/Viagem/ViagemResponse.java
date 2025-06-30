package br.com.rodoviaria.spring_clean_arch.Application.Dto.Response.Viagem;

import br.com.rodoviaria.spring_clean_arch.Application.Dto.Response.Linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.Application.Dto.Response.Onibus.OnibusResponse;

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
