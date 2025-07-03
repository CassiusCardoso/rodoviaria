package br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro;

public record AtualizarInformacoesPassageiroRequest(
        String nome,
        String email,
        String senha,
        String cpf,
        String telefone
) {
}
