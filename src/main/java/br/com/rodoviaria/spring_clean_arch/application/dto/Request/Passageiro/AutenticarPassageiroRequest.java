package br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro;

public record AutenticarPassageiroRequest(
        String email,
        String senha
) {
}
