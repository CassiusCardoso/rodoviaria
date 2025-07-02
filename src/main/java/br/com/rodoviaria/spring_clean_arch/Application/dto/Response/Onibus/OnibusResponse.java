package br.com.rodoviaria.spring_clean_arch.app_temp.dto.Response.Onibus;

import java.util.UUID;

public record OnibusResponse(
        UUID id,
        String placa,
        String modelo,
        int capacidade
        ) {
}
