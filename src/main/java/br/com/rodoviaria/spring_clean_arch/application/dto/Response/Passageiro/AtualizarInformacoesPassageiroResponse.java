package br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro;

import java.time.LocalDateTime;

public record AtualizarInformacoesPassageiroResponse(
        String nome,
        String email,
        String cpf,
        String telefone,
        Boolean ativo,           // 2. Adicione o novo campo
        LocalDateTime criadoEm   // 3. Adicione o novo campo
) {
}
