package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.SenhaEncoderPort;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.PassageiroInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Cpf;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Senha;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Telefone;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // HABILITANDO A INTEGRAÇÃO DO MOCKITO COM JUNIT5

public class DesativarPassageiroUseCaseTest {
    @Mock
    private PassageiroRepository repository;


    @InjectMocks // Cria uma instância real do UseCase e injeta as dependencias necessárias citadas acima
    private DesativarPassageiroUseCase desativarPassageiroUseCase;

    private UUID passageiroId = UUID.randomUUID();
    private Passageiro passageiroAtivo;

    @BeforeEach
    void setUp(){
        passageiroId = UUID.randomUUID();
        passageiroAtivo = new Passageiro(
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
    @DisplayName("Deve desativar um passageiro com sucesso após receber uma requisição válida")
    void deveDesativarPassageiroComSucesso_QuandoRequisicaoValida(){
        // ARRANGE

        // Quando o repositório buscar pelo ID, finja que encontrou nosso 'passageiroAtivo'.

        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.of(passageiroAtivo));

        // Quando o método 'salvar' for chamado, simplesmente retorne o passageiro que foi passado.
        // Isso é útil para verificar se o passageiro desativado foi corretamente passado para o método salvar.
        when(repository.salvar(any(Passageiro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        // Execute o caso de uso. Verificamos se NENHUMA exceção é lançada.
        assertDoesNotThrow(() ->{
            desativarPassageiroUseCase.execute(passageiroId);
        });

        // --- ASSERT (Verificar) ---
        // Verifique se o método 'salvar' foi chamado exatamente uma vez.
        verify(repository, times(1)).salvar(any(Passageiro.class));

    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar desativar um passageiro que não existe")
    void deveLancarExcecao_QuandoPassageiroNaoExiste() {
        // ARRANGE (organizar)
        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.empty());

        // ACT e ASSERT
        // Verificar se a exceção é lançada
        assertThrows(PassageiroInvalidoException.class, () -> {
            desativarPassageiroUseCase.execute(passageiroId);
        });

        // --- VERIFY ---
        // Garanta que o método 'salvar' nunca foi chamado.
        verify(repository, never()).salvar(any(Passageiro.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando passageiro está inativo")
    void deveLancarExcecao_QuandoPassageiroEstaInativo() {
        // ARRANGE
        // Cria uma instância do passageiro desativado, com o método desativar()
        Passageiro passageiroInativo = passageiroAtivo.desativar();

        // ACT
        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.of(passageiroInativo));

        // ASSERT
        assertThrows(PassageiroInvalidoException.class, () -> {
            desativarPassageiroUseCase.execute(passageiroId);
        });

        verify(repository, never()).salvar(any(Passageiro.class));
    }
}
