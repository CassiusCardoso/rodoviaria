package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.AtualizarInformacoesPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.AtualizarInformacoesPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.PassageiroMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.PassageiroInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Cpf;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Senha;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Telefone;

import java.util.UUID;

public class AtualizarInformacoesPassageiroUseCase {
    private final PassageiroRepository passageiroRepository;

    public AtualizarInformacoesPassageiroUseCase(PassageiroRepository passageiroRepository){
        this.passageiroRepository = passageiroRepository;
    }

    public AtualizarInformacoesPassageiroResponse execute(AtualizarInformacoesPassageiroRequest request, UUID passageiroId) {
        // Verificar se o passageiro existe
        Passageiro passageiroAtual = passageiroRepository.buscarPassageiroPorId(passageiroId)
                .orElseThrow(() -> new PassageiroInvalidoException("Não encontramos nenhum passageiro com o identificador " + passageiroId + " no sistema."));

        if (!passageiroAtual.getAtivo()) {
            throw new PassageiroInvalidoException("Não é possível atualizar as informações de uma conta desativada.");
        }


        // 2. CONVERTER E VALIDAR OS DADOS DO REQUEST EM VALUE OBJECTS
        Email novoEmail = new Email(request.email());
        Senha novaSenha = new Senha(request.senha());
        Cpf novoCpf = new Cpf(request.cpf());
        Telefone novoTelefone = new Telefone(request.telefone());

        // EDIT 04/07 10:42
        if (!passageiroAtual.getAtivo()) {
            throw new PassageiroInvalidoException("Não é possível atualizar as informações de uma conta desativada.");
        }
            // 3. Criar uma nova entidade com os dados atualizados
            Passageiro passageiroAtualizado = new Passageiro(
                    passageiroAtual.getId(),
                    request.nome(), //  Novo valor do request
                    novoEmail, // Passa o VO Email
                    novaSenha, // Passa o VO Senha
                    novoCpf, // Passa o VO Cpf
                    novoTelefone, // Passa o VO Telefone
                    passageiroAtual.getAtivo()
            );

            // Persistir o passageiroAtualizado no banco de dados
            Passageiro novoPassageiro = passageiroRepository.salvar(passageiroAtualizado);

            // 5. CONVERTER A ENTIDADE SALVA PARA O DTO DE RESPOSTA USANDO O MAPPER
            return PassageiroMapper.INSTANCE.toAtualizarInformacoesResponse(novoPassageiro);
        }
    }
