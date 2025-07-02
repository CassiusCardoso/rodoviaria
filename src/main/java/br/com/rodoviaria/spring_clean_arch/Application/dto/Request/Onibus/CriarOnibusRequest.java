package br.com.rodoviaria.spring_clean_arch.Application.dto.Request.Onibus;

public record CriarOnibusRequest(
        String placa,
        String modelo,
        int capacidade
) {
}
