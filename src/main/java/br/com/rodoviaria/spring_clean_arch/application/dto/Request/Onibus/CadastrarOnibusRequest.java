package br.com.rodoviaria.spring_clean_arch.application.dto.request.onibus;

public record CadastrarOnibusRequest(
        String placa,
        String modelo,
        int capacidade
) {
}
