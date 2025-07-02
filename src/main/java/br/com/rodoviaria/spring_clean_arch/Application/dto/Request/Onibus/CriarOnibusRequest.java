package br.com.rodoviaria.spring_clean_arch.app_temp.dto.Request.Onibus;

public record CriarOnibusRequest(
        String placa,
        String modelo,
        int capacidade
) {
}
