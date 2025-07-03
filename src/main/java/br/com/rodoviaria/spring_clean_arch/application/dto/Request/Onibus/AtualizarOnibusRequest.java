package br.com.rodoviaria.spring_clean_arch.application.dto.request.onibus;

public record AtualizarOnibusRequest(
        String modelo,
        int capacidade
){
}
