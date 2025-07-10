package br.com.rodoviaria.spring_clean_arch.application.dto.response.admin;

import java.util.UUID;

public record AutenticarAdminResponse(
        UUID id,
        String nome,
        String email,
        String token
) {
}