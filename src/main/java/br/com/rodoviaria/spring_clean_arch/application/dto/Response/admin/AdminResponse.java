package br.com.rodoviaria.spring_clean_arch.application.dto.response.admin;

import java.util.UUID;

// DTO de resposta que NUNCA expõe a senha.
public record AdminResponse(UUID id, String nome, String email, Boolean ativo) {}