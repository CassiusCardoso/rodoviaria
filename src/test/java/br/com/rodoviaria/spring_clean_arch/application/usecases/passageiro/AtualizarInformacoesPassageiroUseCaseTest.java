package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.AtualizarInformacoesPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.AtualizarInformacoesPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.NomeInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.PassageiroInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Cpf;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Senha;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Telefone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AtualizarInformacoesPassageiroUseCaseTest {
    @Mock
    private PassageiroRepository repository;

    @InjectMocks
    private AtualizarInformacoesPassageiroUseCase atualizarInformacoesPassageiroUseCase;

    private UUID passageiroId;
    private Passageiro passageiro;

    @BeforeEach
    void setUp(){
        passageiroId = UUID.randomUUID();
        passageiro = new Passageiro(
                passageiroId,
                "John Doe",
                new Email("john.doe@gmail.com"),
                new Senha("Senha@Valida1"),
                new Cpf("259.174.501-37"),
                new Telefone("(11) 98888-7777"),
                true // O passageiro começa ATIVO
        );
    }

    @Test
    @DisplayName("Deve atualizar um passageiro após receber uma requisição válida")
    void deveAtualizarPassageiroComSucesso_QuandoRequisicaoValida(){

        // ARRANGE
        AtualizarInformacoesPassageiroRequest request = new AtualizarInformacoesPassageiroRequest(
                "John Doe Novo",
                "john.doe.novo@gmail.com",
                "Senha123@",
                "129.425.841-90",
                "(11) 98888-7777"
        );
        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.of(passageiro));

        // Quando o método 'salvar' for chamado, retornar o passageiro atualizado
        when(repository.salvar(any(Passageiro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        AtualizarInformacoesPassageiroResponse response = atualizarInformacoesPassageiroUseCase.execute(request, passageiroId);

        // ASSERT
        assertNotNull(response);
        assertEquals("John Doe Novo", response.nome());
        assertEquals("john.doe.novo@gmail.com", response.email());

        ArgumentCaptor<Passageiro> passageiroCaptor = ArgumentCaptor.forClass(Passageiro.class);

        verify(repository, times(1)).salvar(passageiroCaptor.capture());

        // Pegue o passageiro capturado e verifique se ele contém os dados atualizados.
        Passageiro passageiroSalvo = passageiroCaptor.getValue();
        assertEquals("John Doe Novo", passageiroSalvo.getNome());
        assertEquals("john.doe.novo@gmail.com", passageiroSalvo.getEmail().getEmail());

        // Compare o valor limpo do CPF do request com o valor do objeto salvo.
        String cpfLimpo = request.cpf().replaceAll("[^\\d]", "");
        assertEquals(cpfLimpo, passageiroSalvo.getCpf().getCpf());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o passageiro for inativo")
    void deveLancarExcecao_QuandoPassageiroForInativo(){
        // ARRANGE
        // CRIAR UMA INSTÂNCIA DE UM PASSAGEIRO INATIVO
        // PODE SER O PASSAGEIRO DO setUp()
        Passageiro passageiroInativo = passageiro.desativar();

        // CRIAR UM REQUEST DE ATUALIZAÇÃO QUE SEJA COM QUALQUER CONTEÚDO
        // A EXECUÇÃO TEM QUE FALHAR ANTES DE OS DADOS SEREM USADOS
        AtualizarInformacoesPassageiroRequest request = new AtualizarInformacoesPassageiroRequest(
                "Nome Novo", "email.novo@gmail.com", "NovaSenha@123", "987.654.321-00", "(33) 94444-5555"
        );
        // QUANDO O REPOSITÓRIO BUSCAR O PASSAGEIRO POR ID, FINGIR QUE ENCONTROU O PASSAGEIRO INATIVO

        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.of(passageiroInativo));

        // ACT e ASSERT
        // Verificando se a exceção encontrada é a correta (PassageiroInvalidoException)
        assertThrows(PassageiroInvalidoException.class, () -> {
            atualizarInformacoesPassageiroUseCase.execute(request, passageiroId);
        }, "Deveria ter lançado PassageiroInvalidoException, mas não o fez");

        // VERIFY
        // GARANTIR QUE O MÉTODO salvar nunca foi chamado
        // PORQUE A LÓGICA DEVE PARAR no status ativo
        verify(repository, never()).salvar(any(Passageiro.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o passageiro informar um email inválido para atualizar")
    void deveLancarExcecao_QuandoEmailForInvalido(){

        // ARRANGE
        Passageiro passageiroInvalido = passageiro;

        AtualizarInformacoesPassageiroRequest request = new AtualizarInformacoesPassageiroRequest(
                "John", "Jonh@yahoo.com","123456@", "129.425.841-90", "(11) 96589-1605"
        );

        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.of(passageiroInvalido));

        assertThrows(IllegalArgumentException.class, () -> {
            atualizarInformacoesPassageiroUseCase.execute(request, passageiroId);
        }, "Deveria ter lançado PassageiroInvalidoException, mas não o fez");

        verify(repository, never()).salvar(any(Passageiro.class));

    }

    @Test
    @DisplayName("Deve lançar exceção quando um passageiro não for encontrado")
    void deveLancarExcecao_QuandoPassageiroNaoForEncontrado(){
        // ARRANGE
        Passageiro passageiroNaoEncontrado = null;

        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.ofNullable(passageiroNaoEncontrado));

        AtualizarInformacoesPassageiroRequest request =  new AtualizarInformacoesPassageiroRequest("a", "l", "a", "2", "1");

        assertThrows(PassageiroInvalidoException.class, () -> {
            atualizarInformacoesPassageiroUseCase.execute(request, passageiroId);
        }, "Deveria ter lançado exceção, mas não o fez");

        verify(repository, never()).salvar(any(Passageiro.class));
    }

    @Test
    @DisplayName("Deve lançar uma exceção quando o CPF for inválido")
    void deveLancarExcecao_QuandoCpfForInvalido(){

        // ARRANGE
        Passageiro passageiroInvalido = passageiro;

        AtualizarInformacoesPassageiroRequest request = new AtualizarInformacoesPassageiroRequest(
                "John Travolta", "johntravolta@gmail.com", "123456@", "123.456-789.10", "(11) 96589-1605"
        );

        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.of(passageiroInvalido));

        assertThrows(IllegalArgumentException.class, () -> {
            atualizarInformacoesPassageiroUseCase.execute(request, passageiroId);
        }, "Deveria ter lançado uma exceção mas não lançou");

        verify(repository, never()).salvar(any(Passageiro.class));

    }

    @Test
    @DisplayName("Deve lançar uma exceção quando a senha for inválida.")
    void deveLancarExcecao_QuandoSenhaForInvalida(){
        // ARRANGE
        Passageiro passageiroInvalido = passageiro;

        AtualizarInformacoesPassageiroRequest request = new AtualizarInformacoesPassageiroRequest(
                "Jols", "jols1@gmail.com", "12345", "129.425.841-90", "(11) 96589-1605"
        );
        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.of(passageiroInvalido));

        assertThrows(IllegalArgumentException.class, () -> {
            atualizarInformacoesPassageiroUseCase.execute(request, passageiroId);
        }, "Deveria ter lançado uma exceção mas não lançou.");

        verify(repository, never()).salvar(any(Passageiro.class));
    }

    @Test
    @DisplayName("Deve lançar uma exceção quando o número do telefone for inválido.")
    void deveLancarExcecao_QuandoTelefoneForInvalido(){
        // ARRANGE
        Passageiro passageiroInvalido = passageiro;

        AtualizarInformacoesPassageiroRequest request = new AtualizarInformacoesPassageiroRequest(
                "jan", "jan@gmail.com", "123456@", "129.425.841-90", "11 96589-16025"
        );

        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.of(passageiroInvalido));

        assertThrows(IllegalArgumentException.class, () -> {
            atualizarInformacoesPassageiroUseCase.execute(request, passageiroId);
        }, "Deve lançar uma exceção mas não lançou");


        verify(repository, never()).salvar(any(Passageiro.class));
    }

    @Test
    @DisplayName("Deve lançar uma exceção quando o nome for vazio.")
    void deveLancarExcecao_QuandoNomeForVazio(){
        // ARRANGE
        Passageiro passageiroInvalido = passageiro;

        AtualizarInformacoesPassageiroRequest request = new AtualizarInformacoesPassageiroRequest(
                "", "oller@gmail.com", "12345@g0","129.425.841-90", "(11) 96589-1605"
        );

        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.of(passageiroInvalido));

        assertThrows(NomeInvalidoException.class, () ->{
            atualizarInformacoesPassageiroUseCase.execute(request, passageiroId);
        }, "Deve lançar uma exceção, mas não fez.");

        verify(repository, never()).salvar(any(Passageiro.class));
    }


}
