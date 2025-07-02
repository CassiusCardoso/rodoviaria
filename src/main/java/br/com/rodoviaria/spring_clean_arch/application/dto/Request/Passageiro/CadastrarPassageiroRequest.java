package br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro;

public record CadastrarPassageiroRequest(
        // O ID não deve vir no request de criação, ele será gerado pelo sistema.
        // O Role não deve ser definido pelo usuário, como veremos a seguir.
        String nome,
        String email,
        String cpf,
        String telefone

) {
}
