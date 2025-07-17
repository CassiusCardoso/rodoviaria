package br.com.rodoviaria.spring_clean_arch.application.usecases.admin;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.admin.CriarAdminRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.admin.AdminResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.AdministradorMapper;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.SenhaEncoderPort;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Administrador;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.AdministradorRepository;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Senha;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CriarAdminUseCase {

    private final AdministradorRepository administradorRepository;
    private final SenhaEncoderPort senhaEncoderPort;
    private final AdministradorMapper administradorMapper;

    public CriarAdminUseCase(AdministradorRepository administradorRepository, SenhaEncoderPort senhaEncoderPort, AdministradorMapper administradorMapper) {
        this.administradorRepository = administradorRepository;
        this.senhaEncoderPort = senhaEncoderPort;
        this.administradorMapper = administradorMapper;
    }

    public AdminResponse execute(CriarAdminRequest request) {
        // Verifica se o email já está em uso
        administradorRepository.buscarPorEmail(request.email()).ifPresent(admin -> {
            throw new IllegalArgumentException("Email já cadastrado.");
        });

        // Pega a senha normal e a criptografa
        String senhaCriptografada = senhaEncoderPort.encode(request.senha());

        // Cria a nova entidade Administrador
        Administrador novoAdmin = new Administrador(
                UUID.randomUUID(),
                request.nome(),
                new Email(request.email()),
                Senha.carregar(senhaCriptografada),
                true
        );

        // Salva no banco de dados
        Administrador adminSalvo = administradorRepository.salvar(novoAdmin);

        // Retorna uma resposta segura (sem a senha!)
        return administradorMapper.toAdminResponse(adminSalvo);
    }
}