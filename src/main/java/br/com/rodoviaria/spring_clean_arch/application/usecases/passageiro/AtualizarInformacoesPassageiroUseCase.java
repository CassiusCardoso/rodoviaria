package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.AtualizarInformacoesPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.AtualizarInformacoesPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.passageiro.PassageiroMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.enums.Role;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.PassageiroInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.passageiro.Cpf;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.passageiro.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.passageiro.Senha;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.passageiro.Telefone;

import java.util.UUID;

public class AtualizarInformacoesPassageiroUseCase {
    private final PassageiroRepository passageiroRepository;

    public AtualizarInformacoesPassageiroUseCase(PassageiroRepository passageiroRepository){
        this.passageiroRepository = passageiroRepository;
    }

    public AtualizarInformacoesPassageiroResponse execute(AtualizarInformacoesPassageiroRequest request, UUID passageiroId, UUID usuarioLogadoId, Role usuarioRole){
        // Verificar se o passageiro existe
        Passageiro passageiroAtual =  passageiroRepository.buscarPassageiroPorId(passageiroId)
                .orElseThrow(() -> new PassageiroInvalidoException("Não encontramos nenhum passageiro com o identificador " + passageiroId + " no sistema."));

        // Adicionar validação de autorização aqui
        boolean dono = passageiroAtual.getId().equals(usuarioLogadoId);
        boolean admin = usuarioRole == Role.ADMINISTRADOR;

        if (!dono && !admin) {
            throw new AutorizacaoInvalidaException("O usuário não tem permissão para alterar estas informações.");
        }

        // 2. CONVERTER E VALIDAR OS DADOS DO REQUEST EM VALUE OBJECTS
        Email novoEmail = new Email(request.email());
        Senha novaSenha = new Senha(request.senha());
        Cpf novoCpf = new Cpf(request.cpf());
        Telefone novoTelefone = new Telefone(request.telefone());

        // EDIT 03/07 11:42
        if(!passageiroAtual.getAtivo() == false){
            throw new PassageiroInvalidoException("Você não pode alterar as informações, pois a sua conta está desativada.");
        }
        // 3. Criar uma nova entidade com os dados atualizados
        Passageiro passageiroAtualizado = new Passageiro(
                passageiroAtual.getId(),
                request.nome(), //  Novo valor do request
                novoEmail, // Passa o VO Email
                novaSenha, // Passa o VO Senha
                novoCpf, // Passa o VO Cpf
                novoTelefone, // Passa o VO Telefone
                passageiroAtual.getRole(),
                passageiroAtual.getAtivo()
        );

        // Persistir o passageiroAtualizado no banco de dados
        Passageiro novoPassageiro = passageiroRepository.salvar(passageiroAtualizado);

        // 5. CONVERTER A ENTIDADE SALVA PARA O DTO DE RESPOSTA USANDO O MAPPER
        return PassageiroMapper.INSTANCE.toAtualizarInformacoesResponse(novoPassageiro);
    }
}
