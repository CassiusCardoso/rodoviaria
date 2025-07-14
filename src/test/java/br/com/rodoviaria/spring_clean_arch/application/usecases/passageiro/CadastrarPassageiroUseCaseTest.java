package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.CadastrarPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.PassageiroMapper;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.SenhaEncoderPort;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
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

@ExtendWith(MockitoExtension.class) // HABILITANDO A INTEGRAÇÃO DO MOCKITO COM JUNIT5
public class CadastrarPassageiroUseCaseTest {
    @Mock // Cria um mock (um "dublê") para a interface do repositório
    private PassageiroRepository repository;

    @Mock // Cria um mock para a interface do encoder da senha.
    private SenhaEncoderPort senhaEncoder;

    @Mock
    private PassageiroMapper passageiroMapper;

    @InjectMocks // Cria uma instância real do UseCase e injeta as dependencias necessárias citadas acima
    private CadastrarPassageiroUseCase cadastrarPassageiroUseCase;

    private CadastrarPassageiroRequest request;

    @BeforeEach // Esté método é executado antes de cada teste
    void setUp(){
        // AQUI É PARA CRIAR UM OBJETO DE REQUISIÇÃO PADRÃO PARA USAR NOS TESTES ADIANTE
        request = new CadastrarPassageiroRequest(
                "John Doe",
                "jonh.doe@gmail.com",
                "Senha@123",
                "389.708.991-20",
                "(11) 98888-7777",
                "PASSAGEIRO"
        );
    }

    @Test
    @DisplayName("Deve cadastrar um passageiro com sucesso quando os dados forem válidos")
    void deveCadastrarPassageiroComSucesso_QuandoDadosSaoValidos(){
        // --- ARRANGE (Organizar) ---
        // Aqui é para definir o comportamento esperado dos mocks.

        // Quando o encoder.encode for chamado com qualquer string, retornar "senha_criptografada"
        when(senhaEncoder.encode(any(String.class))).thenReturn("senha_criptografada");

        // Quando o repositório .salvar() for chamado com qualquer objeto Passageiro,
        // retornar o Passageiro
        when(repository.salvar(any(Passageiro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // --- CORREÇÃO AQUI ---
        // Simule o que o mapper deve retornar. Isso evita o NullPointerException.
        when(passageiroMapper.toResponse(any(Passageiro.class)))
                .thenReturn(new PassageiroResponse(UUID.randomUUID(), request.nome(), request.email(), request.cpf(), request.telefone(), true, null));

        // --- ACT (Para agir) ---
        // Executar o método que preciso testar, nesse caso é o cadastrarPassageiro
        PassageiroResponse response = cadastrarPassageiroUseCase.execute(request);

        // --- ASSERT (Verificar se o retorno foi esperado
        // Nesse caso o retorno esperado é de passageiro criado com sucesso
        assertNotNull(response); // VERIFICANDO SE NÃO ESTÁ RETORNANDO NULL NA CRIAÇÃO         PassageiroResponse response = cadastrarPassageiroUseCase.execute(request);
        assertEquals(request.nome(), response.nome()); // VERIFICANDO SE O NOME QUE O PASSAGEIRO INFORMOU NO REQUEST É O MESMO RETORNANDO NO PASSAGEIRO INSTANCIADO
        assertEquals(request.email(), response.email()); // O MESMO PARA O EMAIL
        assertTrue(response.ativo());

        // VERIFICAR SE OS MÉTODOS DOS MOCKS FORAM CHAMADOS COMO ESPERADO
        // GARANTIR QUE A SENHA FOI CRIPTOGRAFADA ANTES DE SALVAR
        verify(senhaEncoder, times(1)).encode("Senha@123");

        // Garante que o passageiro foi salvo no banco
        verify(repository, times(1)).salvar(any(Passageiro.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao passageiro se o CPF já existe no sitema")
    void deveLancarExcecao_QuandoCpfJaExiste(){

        // --- ARRANGE (Organizar) ---
        // O 'request' já foi criado pelo método @BeforeEach, então podemos usá-lo.
        // Configure o comportamento do mock para este cenário específico.
        // Quando o método 'buscarPorCpf' for chamado com o CPF do nosso request,
        // finja que encontrou um passageiro. Retornamos um Optional não vazio.
        // Não precisamos construir um Passageiro completo, um mock já serve.
        when(repository.buscarPorCpf(request.cpf()))
                .thenReturn(Optional.of(mock(Passageiro.class)));

        // --- ACT & ASSERT (Agir e Verificar) ---
        // Verifique se a exceção correta é lançada ao executar o método.
        // O JUnit executará o código dentro do lambda e confirmará se a exceção esperada ocorreu.
        assertThrows(IllegalArgumentException.class, () -> {
            cadastrarPassageiroUseCase.execute(request);
        }, "Deveria ter lançado IllegalArgumentException, mas não o fez");

        // Verifique se os métodos "críticos" NUNCA foram chamados.
        // Se a exceção foi lançada corretamente, o código não deve ter prosseguido
        // para criptografar a senha ou salvar o novo usuário.
        verify(senhaEncoder, never()).encode(anyString());
        verify(repository, never()).salvar(any(Passageiro.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao passageiro se email já existir no sistema")
    void deveLancarExcecao_QuandoEmailJaExiste(){

        // ARRANGE
        when(repository.buscarPorEmail(request.email()))
                .thenReturn(Optional.of(mock(Passageiro.class)));

        // ACT e ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            cadastrarPassageiroUseCase.execute(request);

        }, "Deveria ter lançado IllegalArgumentException, mas não o fez");

        verify(senhaEncoder, never()).encode(anyString());
        verify(repository, never()).salvar(any(Passageiro.class));
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao passageiro quando o CPF for inválido")
    void deveLancarExcecao_QuandoFormatoDoCpfEInvalido(){
        // ARRANGE
        CadastrarPassageiroRequest requestInvalido = new CadastrarPassageiroRequest(
                "John Doe",
                "john.doe@gmail.com",
                "Senha@123",
                "111.222.333-40",
                "(21) 96589-1605",
                "PASSAGEIRO"
        );

        // ACT e ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            cadastrarPassageiroUseCase.execute(requestInvalido);
        });

        verify(repository, never()).salvar(any(Passageiro.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao passageiro se o formato do e-mail for inválido.")
    void deveLancarExcecao_QuandoFormatoDoEmailEInvalido(){
        // ARRANGE
        CadastrarPassageiroRequest requestInvalido = new CadastrarPassageiroRequest(
                "John Doe",
                "johndoe@outlook.com",
                "Senha@123",
                "389.708.991-20",
                "(11) 98888-7777",
                "PASSAGEIRO"
        );

        // ACT e assert
        assertThrows(IllegalArgumentException.class, () -> {
            cadastrarPassageiroUseCase.execute(requestInvalido);
        });

        verify(repository, never()).salvar(any(Passageiro.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao passageiro se o formato da senha for inválido.")
    void deveLancarExcecao_QuandoFormatoDaSenhaEInvalido(){
        // ARRANGE
        CadastrarPassageiroRequest requestInvalido = new CadastrarPassageiroRequest(
                "John Doe",
                "john.doe@gmail.com",
                "1234567890",
                "389.708.991-20",
                "(11) 98888-7777",
                "PASSAGEIRO"
        );

        // ACT e assert
        assertThrows(IllegalArgumentException.class, () -> {
            cadastrarPassageiroUseCase.execute(requestInvalido);
        });
        verify(repository, never()).salvar(any(Passageiro.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao passageiro quando o formato do telefone for inválido")
    void deveLancarExcecao_QuandoFormatoDoTelefoneEInvalido(){
        // ARRANGE
        CadastrarPassageiroRequest requestInvalido = new CadastrarPassageiroRequest(
                "John",
                "john.doe@gmail.com",
                "1234567890",
                "389.708.991-20",
                "11 98888-7777",
                "PASSAGEIRO"
        );

        // ACT e assert
        assertThrows(IllegalArgumentException.class, () -> {
            cadastrarPassageiroUseCase.execute(requestInvalido);
        });
        verify(repository, never()).salvar(any(Passageiro.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nome do passageiro for vazio.")
    void deveLancarExcecao_QuandoNomeEstaVazio(){
        // ARRANGE
        CadastrarPassageiroRequest requestInvalido = new CadastrarPassageiroRequest(
                "",
                "john.doe@gmail.com",
                "1234567890",
                "389.708.991-20",
                "(11) 98888-7777",
                "PASSAGEIRO"
        );
        // ACT e assert
        assertThrows(IllegalArgumentException.class, () -> {
            cadastrarPassageiroUseCase.execute(requestInvalido);
        });
        verify(repository, never()).salvar(any(Passageiro.class));
    }
}
