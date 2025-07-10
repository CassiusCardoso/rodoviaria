package br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro;

import java.time.LocalDateTime;
import java.util.UUID;

public record PassageiroResponse(
        UUID id,
        String nome,
        String email,
        String cpf,
        String telefone,
        Boolean ativo,           // 2. Adicione o novo campo
        LocalDateTime criadoEm   // 3. Adicione o novo campo
) {
}