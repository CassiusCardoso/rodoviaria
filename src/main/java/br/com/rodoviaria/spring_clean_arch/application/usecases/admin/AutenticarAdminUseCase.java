package br.com.rodoviaria.spring_clean_arch.application.usecases.admin;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.admin.AutenticarAdminRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.admin.AutenticarAdminResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.AdministradorMapper;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.SenhaEncoderPort;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.TokenServicePort;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Administrador;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.AdministradorRepository;

public class AutenticarAdminUseCase {
    private final AdministradorRepository administradorRepository;
    private final SenhaEncoderPort encoder;
    private final TokenServicePort tokenService;

    public AutenticarAdminUseCase(AdministradorRepository administradorRepository, SenhaEncoderPort encoder, TokenServicePort tokenService){
        this.administradorRepository = administradorRepository;
        this.encoder = encoder;
        this.tokenService = tokenService;
    }

    public AutenticarAdminResponse execute(AutenticarAdminRequest request){
        // Verifica se o admin existe pelo email
        Administrador admin = administradorRepository.buscarPorEmail(request.email())
                .orElseThrow(() -> new AutorizacaoInvalidaException("Email inválido"));

        // Verificar se a conta está ativa
        if(!admin.getAtivo()){
            throw new AutorizacaoInvalidaException("Esta conta de administrador foi desativada.");
        }

        // Verifica se a senha coincide
        if (!encoder.matches(request.senha(), admin.getSenha().getSenhaHash())) {
            throw new AutorizacaoInvalidaException("Email ou senha inválidos.");
        }
        // Gera o token JWT
        String token = tokenService.gerarToken(admin);

        // Retorna a resposta usando o mapper
        return AdministradorMapper.INSTANCE.toResponse(admin, token);
    }
}
