package br.com.rodoviaria.spring_clean_arch.Application.Dto.Request.Onibus;

public record CriarOnibusRequest(
        String placa,
        String modelo,
        int capacidade
) {
}
