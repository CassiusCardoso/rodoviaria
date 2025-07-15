// Conteúdo corrigido para AutenticarAdminUseCaseTest.java
package br.com.rodoviaria.spring_clean_arch.application.usecases.admin;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.admin.AutenticarAdminRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.admin.AutenticarAdminResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.AdministradorMapper;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.SenhaEncoderPort;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.TokenServicePort;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Administrador;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.AdministradorRepository;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Senha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AutenticarAdminUseCaseTest {

    @Mock
    private AdministradorRepository repository;
    @Mock
    private SenhaEncoderPort encoder;
    @Mock
    private TokenServicePort service;
    @Mock
    private AdministradorMapper administradorMapper;
    @InjectMocks
    private AutenticarAdminUseCase autenticarAdminUseCase;

    private UUID adminId;
    private Administrador administrador;
    private AutenticarAdminRequest request;

    @BeforeEach
    void setUp() {
        adminId = UUID.randomUUID();
        String senhaBruta = "12345@admin";
        String senhaCodificada = "senha_codificada";
        String email = "admin@gmail.com";

        // CORREÇÃO AQUI:
        administrador = new Administrador(
                adminId, "Admin", new Email(email), Senha.carregar(senhaCodificada), true // <<-- MUDANÇA
        );
        request = new AutenticarAdminRequest(email, senhaBruta);
    }
    // O resto do arquivo permanece igual
    @Test
    @DisplayName("Deve autenticar um administrador com sucesso quando os dados forem válidos.")
    void deveAutenticarAdmin_QuandoInformacoesSaoValidas(){
        when(repository.buscarPorEmail(administrador.getEmail().getEmail())).thenReturn(Optional.of(administrador));
        when(encoder.matches(request.senha(), administrador.getSenha().getSenhaHash())).thenReturn(true);
        when(service.gerarToken(any(Administrador.class))).thenReturn("token_teste");
        AutenticarAdminResponse responseFalsa = new AutenticarAdminResponse(adminId, administrador.getNome(), administrador.getEmail().getEmail(), "token_teste");
        when(administradorMapper.toResponse(any(Administrador.class), anyString())).thenReturn(responseFalsa);
        AutenticarAdminResponse response = autenticarAdminUseCase.execute(request);
        assertNotNull(response);
        assertEquals(administrador.getId(), response.id());
        assertEquals("token_teste", response.token());
        verify(repository, times(1)).buscarPorEmail(administrador.getEmail().getEmail());
        verify(encoder, times(1)).matches(request.senha(), administrador.getSenha().getSenhaHash());
        verify(service, times(1)).gerarToken(administrador);
        verify(administradorMapper, times(1)).toResponse(administrador, "token_teste");
    }

    @Test
    @DisplayName("Deve lançar exceção e não autenticar um administrador quando ele estiver inativo")
    void deveLancarExcecaoENaoAutenticar_QuandoAdminForInativo(){
        Administrador adminInativo = administrador.desativar();
        when(repository.buscarPorEmail(administrador.getEmail().getEmail())).thenReturn(Optional.of(adminInativo));
        assertThrows(AutorizacaoInvalidaException.class, () -> {
            autenticarAdminUseCase.execute(request);
        });
        verify(encoder, never()).matches(any(String.class), any(String.class));
        verify(service, never()).gerarToken(any(Administrador.class));
        verify(administradorMapper, never()).toResponse(any(Administrador.class), anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando administrador tiver uma senha inválida")
    void deveLancarExcecaoENaoAutenticar_QuandoSenhaForIncorreta(){
        when(repository.buscarPorEmail(administrador.getEmail().getEmail())).thenReturn(Optional.of(administrador));
        when(encoder.matches(request.senha(), administrador.getSenha().getSenhaHash())).thenReturn(false);
        assertThrows(AutorizacaoInvalidaException.class, () -> {
            autenticarAdminUseCase.execute(request);
        });
        verify(encoder, times(1)).matches(request.senha(), administrador.getSenha().getSenhaHash());
        verify(service, never()).gerarToken(any(Administrador.class));
        verify(administradorMapper, never()).toResponse(any(Administrador.class), anyString());
    }
}