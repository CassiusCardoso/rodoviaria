// Conteúdo para CadastrarPassageiroUseCase.java
package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.CadastrarPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.PassageiroMapper;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.SenhaEncoderPort;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Cpf;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Senha;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Telefone;
import java.util.UUID;

public class CadastrarPassageiroUseCase {
    private final PassageiroRepository passageiroRepository;
    private final SenhaEncoderPort encoder;
    private final PassageiroMapper passageiroMapper;

    public CadastrarPassageiroUseCase(PassageiroRepository passageiroRepository, SenhaEncoderPort encoder, PassageiroMapper passageiroMapper) {
        this.passageiroRepository = passageiroRepository;
        this.encoder = encoder;
        this.passageiroMapper = passageiroMapper;
    }

    public PassageiroResponse execute(CadastrarPassageiroRequest request) {
        passageiroRepository.buscarPorEmail(request.email()).ifPresent(p -> {
            throw new IllegalArgumentException("Email já cadastrado.");
        });
        passageiroRepository.buscarPorCpf(request.cpf()).ifPresent(p -> {
            throw new IllegalArgumentException("CPF já cadastrado.");
        });

        Email email = new Email(request.email());
        Cpf cpf = new Cpf(request.cpf());
        Telefone telefone = new Telefone(request.telefone());

        // --- CORREÇÃO AQUI ---
        // 1. Valida o formato da senha bruta.
        Senha.validarFormato(request.senha());
        // 2. Criptografa a senha.
        String senhaCodificada = encoder.encode(request.senha());
        // 3. Carrega o hash na entidade.
        Senha senhaSegura = Senha.carregar(senhaCodificada);

        Passageiro novoPassageiro = new Passageiro(
                UUID.randomUUID(),
                request.nome(),
                email,
                senhaSegura,
                cpf,
                telefone,
                true
        );

        Passageiro passageiroSalvo = passageiroRepository.salvar(novoPassageiro);
        return passageiroMapper.toResponse(passageiroSalvo);
    }
}