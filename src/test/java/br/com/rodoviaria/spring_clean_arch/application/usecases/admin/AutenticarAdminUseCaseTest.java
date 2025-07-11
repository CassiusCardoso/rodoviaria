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
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void setUp(){
        adminId = UUID.randomUUID();
        String senhaBruta = "12345@admin";
        String senhaCodificada = "senha_codificada";
        String email = "admin@gmail.com";
        administrador = new Administrador(
                adminId, "Admin", new Email(email), new Senha(senhaCodificada), true
        );
        request = new AutenticarAdminRequest(email, senhaBruta);
    }

    @Test
    @DisplayName("Deve autenticar um administrador com sucesso quando os dados forem válidos.")
    void deveAutenticarAdmin_QuandoInformacoesSaoValidas(){
        // ARRANGE
        when(repository.buscarPorEmail(administrador.getEmail().getEmail())).thenReturn(Optional.of(administrador));

        // Quando o encoder comparar a senha bruta do request com a senha (hash) do passageiro, retorne 'true'.
        when(encoder.matches(request.senha(), administrador.getSenha().getSenhaHash())).thenReturn(true);

        // Quando o serviço de token for chamado para gerar um token, retornar um token_teste
        when(service.gerarToken(any(Administrador.class))).thenReturn("token_teste");

        // ACT para chamar o caso de uso de autenticaradmin
        // Mock do Mapper: Crie uma resposta falsa que o mapper deve retornar
        AutenticarAdminResponse responseFalsa = new AutenticarAdminResponse(
                adminId, administrador.getNome(), administrador.getEmail().getEmail(), "token_teste"
        );

        when(administradorMapper.toResponse(any(Administrador.class), anyString())).thenReturn(responseFalsa);

        // ACT
        AutenticarAdminResponse response = autenticarAdminUseCase.execute(request);

        // ASSERT
        assertNotNull(response);
        assertEquals(administrador.getId(), response.id());
        assertEquals("token_teste", response.token());

        // VERIFY
        // Para verificar se os métodos mockados (service, repository, encoder) foram chamados
        verify(repository, times(1)).buscarPorEmail(administrador.getEmail().getEmail());
        verify(encoder, times(1)).matches(request.senha(), administrador.getSenha().getSenhaHash());
        verify(service, times(1)).gerarToken(administrador);
        verify(administradorMapper, times(1)).toResponse(administrador, "token_teste");
    }

    @Test
    @DisplayName("Deve lançar exceção e não autenticar um administrador quando ele estiver inativo")
    void deveLancarExcecaoENaoAutenticar_QuandoAdminForInativo(){
        // ARRANGE
        // simular que encontrou o admin pelo email
        Administrador adminInativo = administrador.desativar();
        when(repository.buscarPorEmail(administrador.getEmail().getEmail())).thenReturn(Optional.of(adminInativo));

        // ACT e ASSERT
        assertThrows(AutorizacaoInvalidaException.class, () -> {
            autenticarAdminUseCase.execute(request);
        });

        // VERIFY
        // Garanta que, apesar de o passageiro ter sido encontrado, o fluxo não prosseguiu
        // para a verificação da senha ou para a geração do token.
        verify(encoder, never()).matches(any(String.class), any(String.class));
        verify(service, never()).gerarToken(any(Administrador.class));
        verify(administradorMapper, never()).toResponse(any(Administrador.class), anyString());


    }

    @Test
    @DisplayName("Deve lançar exceção quando administrador tiver uma senha inválida")
    void deveLancarExcecaoENaoAutenticar_QuandoSenhaForIncorreta(){
        // Simular que encontrou um admin pelo email
        when(repository.buscarPorEmail(administrador.getEmail().getEmail())).thenReturn(Optional.of(administrador));

        // verificar se a senha coincide e retornar false
        when(encoder.matches(request.senha(), administrador.getSenha().getSenhaHash())).thenReturn(false);

        // ACT e ASSERT
        assertThrows(AutorizacaoInvalidaException.class, () -> {
            autenticarAdminUseCase.execute(request);
        });

        // VERIFY
        verify(encoder, times(1)).matches(request.senha(), administrador.getSenha().getSenhaHash());
        verify(service, never()).gerarToken(any(Administrador.class));
        verify(administradorMapper, never()).toResponse(any(Administrador.class), anyString());
    }

}
