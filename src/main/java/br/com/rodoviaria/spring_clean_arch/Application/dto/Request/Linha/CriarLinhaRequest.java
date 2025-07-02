package br.com.rodoviaria.spring_clean_arch.Application.dto.Request.Linha;

public record CriarLinhaRequest(
        String origem,
        String destino,
        int duracaoPrevistaMinutos
) {
}
