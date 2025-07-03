// Em: br/com/rodoviaria/spring_clean_arch/application/usecases/passageiro/CadastrarPassageiroUseCase.java

package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

// Imports...
import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.CadastrarPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.passageiro.PassageiroMapper;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.SenhaEncoderPort;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.enums.Role;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.passageiro.Cpf;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.passageiro.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.passageiro.Senha;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.passageiro.Telefone;

import java.util.UUID;

public class CadastrarPassageiroUseCase {
    private final PassageiroRepository passageiroRepository;
    private final SenhaEncoderPort encoder; // 2. ADICIONE A DEPENDÊNCIA

    public CadastrarPassageiroUseCase(PassageiroRepository passageiroRepository, SenhaEncoderPort encoder) {
        this.passageiroRepository = passageiroRepository;
        this.encoder = encoder;// 3. INJETE NO CONSTRUTOR
    }

    public PassageiroResponse execute(CadastrarPassageiroRequest request) {

        // ... verificações de duplicidade de e-mail e CPF (continuam iguais) ...

        // ... criação dos VOs de Email, Cpf e Telefone (continuam iguais) ...
        Email email = new Email(request.email());
        Cpf cpf = new Cpf(request.cpf());
        Telefone telefone = new Telefone(request.telefone());

        // 4. LÓGICA DE CRIPTOGRAFIA DA SENHA
        // Pega a senha bruta do request.
        String senhaBruta = request.senha();
        // Usa o encoder para criar o hash seguro.
        String senhaCodificada = encoder.encode(senhaBruta);

        // Cria o Value Object Senha com a senha JÁ CODIFICADA.
        Senha senhaSegura = new Senha(senhaCodificada);

        // 5. CRIAR A ENTIDADE DE DOMÍNIO
        Passageiro novoPassageiro = new Passageiro(
                UUID.randomUUID(),
                request.nome(),
                email,
                senhaSegura, // <<-- AGORA PASSA A SENHA SEGURA
                cpf,
                telefone,
                Role.PASSAGEIRO,
                true // Um novo passageiro sempre nasce ativo
        );

        // ... Persistir e Mapear para o Response (continuam iguais) ...
        Passageiro passageiroSalvo = passageiroRepository.salvar(novoPassageiro);
        return PassageiroMapper.INSTANCE.toResponse(passageiroSalvo);
    }
}