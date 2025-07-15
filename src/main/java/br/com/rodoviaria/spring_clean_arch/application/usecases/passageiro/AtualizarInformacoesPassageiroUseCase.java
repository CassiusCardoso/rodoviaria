package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.AtualizarInformacoesPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.AtualizarInformacoesPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.PassageiroMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.PassageiroInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Cpf;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Senha;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Telefone;

import java.util.UUID;

public class AtualizarInformacoesPassageiroUseCase {
    private final PassageiroRepository passageiroRepository;
    private final PassageiroMapper passageiroMapper;

    public AtualizarInformacoesPassageiroUseCase(PassageiroRepository passageiroRepository, PassageiroMapper passageiroMapper){
        this.passageiroRepository = passageiroRepository;
        this.passageiroMapper = passageiroMapper;
    }

    public AtualizarInformacoesPassageiroResponse execute(AtualizarInformacoesPassageiroRequest request, UUID passageiroId) {
        Passageiro passageiroAtual = passageiroRepository.buscarPassageiroPorId(passageiroId)
                .orElseThrow(() -> new PassageiroInvalidoException("Não encontramos nenhum passageiro com o identificador " + passageiroId + " no sistema."));

        if (!passageiroAtual.getAtivo()) {
            throw new PassageiroInvalidoException("Não é possível atualizar as informações de uma conta desativada.");
        }

        Email novoEmail = new Email(request.email());
        Cpf novoCpf = new Cpf(request.cpf());
        Telefone novoTelefone = new Telefone(request.telefone());

        // --- CORREÇÃO AQUI ---
        // 1. Valida o formato da nova senha em texto plano.
        Senha.validarFormato(request.senha());
        // 2. Carrega a senha (ainda em texto plano) no ValueObject.
        // A criptografia acontecerá mais tarde se necessário.
        Senha novaSenha = Senha.carregar(request.senha());
        // OBS: Se a senha precisar ser criptografada na atualização,
        // o encoder seria chamado aqui antes de carregar.

        Passageiro passageiroAtualizado = new Passageiro(
                passageiroAtual.getId(),
                request.nome(),
                novoEmail,
                novaSenha, // Agora estamos passando um objeto Senha criado corretamente
                novoCpf,
                novoTelefone,
                passageiroAtual.getAtivo()
        );

        Passageiro novoPassageiro = passageiroRepository.salvar(passageiroAtualizado);
        return passageiroMapper.toAtualizarInformacoesResponse(novoPassageiro);
    }
}