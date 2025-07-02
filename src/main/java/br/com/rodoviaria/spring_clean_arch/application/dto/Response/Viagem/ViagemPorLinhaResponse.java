package br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record ViagemPorLinhaResponse(
        LocalDateTime dataPartida,
        LocalDateTime dataChegada,
        LinhaResponse linha,
        UUID onibusId
) {}

