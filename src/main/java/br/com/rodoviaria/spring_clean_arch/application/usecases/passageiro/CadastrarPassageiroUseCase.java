package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.CadastrarPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.passageiro.PassageiroMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.enums.Role;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Cpf;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Senha;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Telefone;

import java.util.UUID;

public class CadastrarPassageiroUseCase {
    private final PassageiroRepository passageiroRepository;
    public CadastrarPassageiroUseCase(PassageiroRepository passageiroRepository) {
        this.passageiroRepository = passageiroRepository;
    }

    public PassageiroResponse execute(CadastrarPassageiroRequest request) {
        // 1. VERIFICAR DUPLICIDADE (Regra de Negócio da Aplicação)
        // Usamos os contratos do repositório para verificar se já existe um usuário
        // com o mesmo e-mail ou CPF antes de prosseguir.
        passageiroRepository.buscarPorEmail(request.email())
                .ifPresent(p -> { throw new RuntimeException("Email já cadastrado."); });

        passageiroRepository.buscarPorCpf(request.cpf())
                .ifPresent(p -> { throw new RuntimeException("CPF já cadastrado."); });

        // 2. CONVERTER E VALIDAR DADOS DE ENTRADA (Criação dos Value Objects)
        // Se qualquer um dos dados no request for inválido, o construtor do VO
        // irá lançar uma exceção e a execução do caso de uso irá parar aqui.
        Email email = new Email(request.email());
        Cpf cpf = new Cpf(request.cpf());
        Telefone telefone = new Telefone(request.telefone());

        // Para a senha, por enquanto vamos apenas validar o formato.
        // A criptografia seria um passo adicional aqui.
        Senha senha = new Senha(request.senha());

        // 3. CRIAR A ENTIDADE DE DOMÍNIO
        // Agora, com os dados validados e convertidos para os tipos de domínio,
        // nós criamos a nossa entidade Passageiro.
        Passageiro novoPassageiro = new Passageiro(
                UUID.randomUUID(),
                request.nome(),
                email,
                senha, // Value Object
                cpf,
                telefone,
                Role.PASSAGEIRO // O Role precisa ser definido automaticamente na hora que o objeto é criado, e é definido pelo sistema, não pelo passageiro
        );
        // Persistir a entidade
        Passageiro passageiroSalvo = passageiroRepository.salvar(novoPassageiro);
        // 5. MAPEAR PARA O DTO DE RESPOSTA E RETORNAR
        return PassageiroMapper.INSTANCE.toResponse(passageiroSalvo);
    }
}
