package br.com.rodoviaria.spring_clean_arch.application.dto.request.admin;

public record AutenticarAdminRequest(
        String email,
        String senha
) {
}
