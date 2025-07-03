package br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro;

public record AtualizarInformacoesPassageiroResponse(
        String nome,
        String email,
        String senha,
        String cpf,
        String telefone
) {
}
