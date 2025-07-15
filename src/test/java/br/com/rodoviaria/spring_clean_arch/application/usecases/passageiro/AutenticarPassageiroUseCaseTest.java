// Conteúdo corrigido para AutenticarPassageiroUseCaseTest.java
package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.AutenticarPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.AutenticarPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.SenhaEncoderPort;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.TokenServicePort;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Cpf;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Senha;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Telefone;
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
public class AutenticarPassageiroUseCaseTest {

    @Mock
    private PassageiroRepository repository;
    @Mock
    private SenhaEncoderPort encoder;
    @Mock
    private TokenServicePort tokenService;
    @InjectMocks
    private AutenticarPassageiroUseCase autenticarPassageiroUseCase;

    private AutenticarPassageiroRequest request;
    private UUID passageiroId;
    private Passageiro passageiro;

    @BeforeEach
    void setUp() {
        passageiroId = UUID.randomUUID();
        String senhaBruta = "12345@grussel";
        String senhaCodificada = "senha_codificada_valida";
        String email = "georgerussel@gmail.com";

        // CORREÇÃO AQUI: Usa Senha.carregar() para criar o objeto
        passageiro = new Passageiro(
                passageiroId,
                "George Russel",
                new Email(email),
                Senha.carregar(senhaCodificada), // <<-- MUDANÇA
                new Cpf("389.708.991-20"),
                new Telefone("(11) 98888-7777"),
                true
        );
        request = new AutenticarPassageiroRequest(email, senhaBruta);
    }

    // O resto do arquivo permanece igual
    @Test
    @DisplayName("Deve autenticar um passageiro com sucesso quando os dados forem válidos")
    void deveAutenticarPassageiro_QuandoInformacoesSaoValidas(){
        when(repository.buscarPorEmail(passageiro.getEmail().getEmail())).thenReturn(Optional.of(passageiro));
        when(encoder.matches(request.senha(), passageiro.getSenha().getSenhaHash())).thenReturn(true);
        when(tokenService.gerarToken(any(Passageiro.class))).thenReturn("token_teste");
        AutenticarPassageiroResponse response = autenticarPassageiroUseCase.execute(request);
        assertNotNull(response);
        assertEquals(passageiro.getId(), response.passageiroId());
        assertEquals(passageiro.getNome(), response.nome());
        assertEquals("token_teste", response.token());
        verify(repository, times(1)).buscarPorEmail(passageiro.getEmail().getEmail());
        verify(encoder, times(1)).matches(request.senha(), passageiro.getSenha().getSenhaHash());
        verify(tokenService, times(1)).gerarToken(passageiro);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a senha for inválida")
    void deveLancarExcecao_QuandoSenhaForIncorreta(){
        when(repository.buscarPorEmail(passageiro.getEmail().getEmail())).thenReturn(Optional.of(passageiro));
        when(encoder.matches(request.senha(), passageiro.getSenha().getSenhaHash())).thenReturn(false);
        assertThrows(AutorizacaoInvalidaException.class, () -> {
            autenticarPassageiroUseCase.execute(request);
        });
        verify(repository, times(1)).buscarPorEmail(passageiro.getEmail().getEmail());
        verify(encoder, times(1)).matches(request.senha(), passageiro.getSenha().getSenhaHash());
        verify(tokenService,never()).gerarToken(passageiro);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o passageiro não tiver um email encontrado.")
    void deveLancarExcecao_QuandoEmailNaoForEncontrado(){
        when(repository.buscarPorEmail(passageiro.getEmail().getEmail())).thenReturn(Optional.empty());
        assertThrows(AutorizacaoInvalidaException.class, () -> {
            autenticarPassageiroUseCase.execute(request);
        });
        verify(encoder, never()).matches(any(String.class), any(String.class));
        verify(tokenService, never()).gerarToken(any(Passageiro.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando passageiro não for autenticado, porque é inativo")
    void deveLancarExcecaoENaoAutenticar_QuandoPassageiroForInativo(){
        Passageiro passageiroInativo = passageiro.desativar();
        when(repository.buscarPorEmail(passageiro.getEmail().getEmail())).thenReturn(Optional.of(passageiroInativo));
        assertThrows(AutorizacaoInvalidaException.class, () -> {
            autenticarPassageiroUseCase.execute(request);
        });
        verify(encoder, never()).matches(any(String.class), any(String.class));
        verify(tokenService, never()).gerarToken(any(Passageiro.class));
    }
}