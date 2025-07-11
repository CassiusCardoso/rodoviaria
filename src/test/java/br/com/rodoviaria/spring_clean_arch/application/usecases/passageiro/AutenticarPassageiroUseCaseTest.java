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
import static org.mockito.internal.verification.VerificationModeFactory.times;

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
    void setUp(){
        passageiroId = UUID.randomUUID();
        String senhaBruta = "12345@grussel";
        String senhaCodificada = "senha_codificada";
        String email = "georgerussel@gmail.com";
        passageiro = new Passageiro(
                passageiroId,
                "George Russel",
                new Email(email),
                new Senha(senhaCodificada), // O passageiro no banco tem a senha com hash
                new Cpf("389.708.991-20"),
                new Telefone("(11) 98888-7777"),
                true
        );
        request = new AutenticarPassageiroRequest(email, senhaBruta);
    }

    @Test
    @DisplayName("Deve autenticar um passageiro com sucesso quando os dados forem válidos")
    void deveAutenticarPassageiro_QuandoInformacoesSaoValidas(){

        // ARRANGE
        // Quando o repositório buscar por email, retorne o nosso passageiro de teste.
        when(repository.buscarPorEmail(passageiro.getEmail().getEmail())).thenReturn(Optional.of(passageiro));

        // Quando o encoder comparar a senha bruta do request com a senha (hash) do passageiro, retorne 'true'.
        when(encoder.matches(request.senha(), passageiro.getSenha().getSenhaHash())).thenReturn(true);

        // Quando o serviço de token for chamado para gerar um token, retorne um token teste.
        when(tokenService.gerarToken(any(Passageiro.class))).thenReturn("token_teste");

        // ACT - chamar o caso de uso
        AutenticarPassageiroResponse response = autenticarPassageiroUseCase.execute(request);

        // --- ASSERT ---
        // Verifique se a resposta contém os dados esperados.
        assertNotNull(response);
        assertEquals(passageiro.getId(), response.passageiroId());
        assertEquals(passageiro.getNome(), response.nome());
        assertEquals("token_teste", response.token());

        // VERIFY
        // Confirmar que todos os métodos mockados foram chamados
        verify(repository, times(1)).buscarPorEmail(passageiro.getEmail().getEmail());
        verify(encoder, times(1)).matches(request.senha(), passageiro.getSenha().getSenhaHash());
        verify(tokenService, times(1)).gerarToken(passageiro);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a senha for inválida")
    void deveLancarExcecao_QuandoSenhaForIncorreta(){
        // ARRANGE

        // Simular que o passageiro foi encontrado pelo email
        when(repository.buscarPorEmail(passageiro.getEmail().getEmail())).thenReturn(Optional.of(passageiro));

        // Simular a comparação das senhas como false
        when(encoder.matches(request.senha(), passageiro.getSenha().getSenhaHash())).thenReturn(false);

        // ACT e ASSERT
        assertThrows(AutorizacaoInvalidaException.class, () -> {
            autenticarPassageiroUseCase.execute(request);
        });

        // --- VERIFY ---
        //  Garanta que, como a senha falhou, o serviço de token NUNCA foi chamado.
        verify(repository, times(1)).buscarPorEmail(passageiro.getEmail().getEmail());
        verify(encoder, times(1)).matches(request.senha(), passageiro.getSenha().getSenhaHash());
        verify(tokenService,never()).gerarToken(passageiro);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o passageiro não tiver um email encontrado.")
    void deveLancarExcecao_QuandoEmailNaoForEncontrado(){
        // ARRANGE
        //    Quando o repositório buscar pelo e-mail do request, retorne um Optional vazio.
        when(repository.buscarPorEmail(passageiro.getEmail().getEmail())).thenReturn(Optional.empty());

        assertThrows(AutorizacaoInvalidaException.class, () -> {
            autenticarPassageiroUseCase.execute(request);
        });

        // VERIFY
        verify(encoder, never()).matches(any(String.class), any(String.class));
        verify(tokenService, never()).gerarToken(any(Passageiro.class));
    }
    @Test
    @DisplayName("Deve lançar exceção quando passageiro não for autenticado, porque é inativo")
    void deveLancarExcecaoENaoAutenticar_QuandoPassageiroForInativo(){
        // ARRANGE
        Passageiro passageiroInativo = passageiro.desativar();
        when(repository.buscarPorEmail(passageiro.getEmail().getEmail())).thenReturn(Optional.of(passageiroInativo));

        // NOTA: Não precisamos mockar o encoder.matches() ou o tokenService.gerarToken()
        // porque a execução deve parar antes de chegar neles.

        // ACT e ASSERT
        // Verifique se a exceção correta é lançada devido à regra de negócio 'if(!passageiro.getAtivo())'.
        assertThrows(AutorizacaoInvalidaException.class, () -> {
            autenticarPassageiroUseCase.execute(request);
        });

        // --- VERIFY ---
        // Garanta que, apesar de o passageiro ter sido encontrado, o fluxo não prosseguiu
        // para a verificação da senha ou para a geração do token.
        verify(encoder, never()).matches(any(String.class), any(String.class));
        verify(tokenService, never()).gerarToken(any(Passageiro.class));
    }
}
