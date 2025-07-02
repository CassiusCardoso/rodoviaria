package br.com.rodoviaria.spring_clean_arch.application.dto.request.linha;

public record CriarLinhaRequest(
        String origem,
        String destino,
        int duracaoPrevistaMinutos
) {
}
