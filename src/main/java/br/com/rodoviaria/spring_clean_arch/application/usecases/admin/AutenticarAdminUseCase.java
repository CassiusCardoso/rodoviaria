package br.com.rodoviaria.spring_clean_arch.application.usecases.admin;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.admin.AutenticarAdminRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.admin.AutenticarAdminResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.AdministradorMapper;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.SenhaEncoderPort;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.TokenServicePort;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Administrador;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.AdministradorRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.security.AdminAutenticado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AutenticarAdminUseCase {

    private static final Logger logger = LoggerFactory.getLogger(AutenticarAdminUseCase.class);

    private final AdministradorRepository administradorRepository;
    private final SenhaEncoderPort encoder;
    private final TokenServicePort tokenService;
    private final AdministradorMapper administradorMapper;

    public AutenticarAdminUseCase(
            AdministradorRepository administradorRepository,
            SenhaEncoderPort encoder,
            TokenServicePort tokenService,
            AdministradorMapper administradorMapper) {
        this.administradorRepository = administradorRepository;
        this.encoder = encoder;
        this.tokenService = tokenService;
        this.administradorMapper = administradorMapper;
    }

    // Método principal usado no login (AdminController)
    public AutenticarAdminResponse execute(AutenticarAdminRequest request) {
        logger.debug("Tentando autenticar administrador com email: {}", request.email());

        Administrador admin = administradorRepository.buscarPorEmail(request.email())
                .orElseThrow(() -> new AutorizacaoInvalidaException("Email inválido"));

        if (!admin.getAtivo()) {
            logger.warn("Conta de administrador com email {} está desativada.", request.email());
            throw new AutorizacaoInvalidaException("Esta conta de administrador foi desativada.");
        }

        logger.debug("Hash da senha no banco: {}", admin.getSenha().getSenhaHash());
        logger.debug("Senha fornecida: {}", request.senha());

        if (!encoder.matches(request.senha(), admin.getSenha().getSenhaHash())) {
            logger.error("Falha na autenticação: senha fornecida não corresponde ao hash.");
            throw new AutorizacaoInvalidaException("Email ou senha inválidos.");
        }

        String token = tokenService.gerarToken(admin);

        return administradorMapper.toResponse(admin, token);
    }

    // Método alternativo (sem DTO) - pode ser mantido se for usado em testes ou outras APIs
    public String autenticar(String email, String senha) {
        var administrador = administradorRepository.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Administrador não encontrado"));

        boolean senhaValida = encoder.matches(senha, administrador.getSenha().getSenhaHash());
        if (!senhaValida) {
            throw new RuntimeException("Senha inválida");
        }

        return tokenService.gerarToken(administrador);
    }

    // Novo método usado no filtro AdminSecurityFilter (valida token e retorna UserDetails)
    public AdminAutenticado autenticar(String token) {
        String email = tokenService.validarToken(token);
        return administradorRepository.buscarPorEmail(email)
                .map(AdminAutenticado::new)
                .orElse(null); // ou lançar exceção se preferir
    }
}
