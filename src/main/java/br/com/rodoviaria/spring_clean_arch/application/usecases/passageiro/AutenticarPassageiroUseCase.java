package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.AutenticarPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.AutenticarPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.SenhaEncoderPort;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.TokenServicePort;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.PassageiroInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;

public class AutenticarPassageiroUseCase {
    private final PassageiroRepository passageiroRepository;
    private final SenhaEncoderPort encoder;
    private final TokenServicePort service;

    public AutenticarPassageiroUseCase(PassageiroRepository passageiroRepository, SenhaEncoderPort encoder, TokenServicePort service ) {
        this.passageiroRepository = passageiroRepository;
        this.encoder = encoder;
        this.service = service;
    }

    public AutenticarPassageiroResponse execute(AutenticarPassageiroRequest request){
        // Verificar se o passageiro existe pelo email
        Passageiro passageiro = passageiroRepository.buscarPorEmail(request.email())
                .orElseThrow(() -> new AutorizacaoInvalidaException("Email ou senha inválidos."));


        // Verificar se a conta está ativa
        if(!passageiro.getAtivo()){
            throw new AutorizacaoInvalidaException("Esta conta de usuário foi desativada.");
        }
        // Verificar se a senha que o passageiro forneceu coincide com a senha armazenada
        String senhaBruta = request.senha();
        String senhaCodificada = passageiro.getSenha().getSenhaHash();

        if(!encoder.matches(senhaBruta, senhaCodificada)){
            throw new AutorizacaoInvalidaException("Email ou senha inválidos");
        }

        // 4. GERAR O TOKEN DE ACESSO JWT
        // Se a senha estiver correta, usamos o TokenServicePort para gerar o token.
        String token = service.gerarToken(passageiro);

        // Retornar a resposta com token
        return new AutenticarPassageiroResponse(
                passageiro.getId(),
                passageiro.getNome(),
                passageiro.getEmail().getEmail(),
                token
        );
    }
}
