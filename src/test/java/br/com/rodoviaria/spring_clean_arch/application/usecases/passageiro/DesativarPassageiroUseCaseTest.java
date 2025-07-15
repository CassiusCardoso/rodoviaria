// Conteúdo corrigido para DesativarPassageiroUseCaseTest.java
package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DesativarPassageiroUseCaseTest {
    @Mock
    private PassageiroRepository repository;
    @InjectMocks
    private DesativarPassageiroUseCase desativarPassageiroUseCase;

    private UUID passageiroId;
    private Passageiro passageiroAtivo;

    @BeforeEach
    void setUp() {
        passageiroId = UUID.randomUUID();
        // CORREÇÃO AQUI:
        passageiroAtivo = new Passageiro(
                passageiroId,
                "John Doe",
                new Email("john.doe@gmail.com"),
                Senha.carregar("Senha@Valida1"), // <<-- MUDANÇA
                new Cpf("259.174.501-37"),
                new Telefone("(11) 98888-7777"),
                true
        );
    }

    // O resto do arquivo permanece igual
    @Test
    @DisplayName("Deve desativar um passageiro com sucesso após receber uma requisição válida")
    void deveDesativarPassageiroComSucesso_QuandoRequisicaoValida(){
        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.of(passageiroAtivo));
        when(repository.salvar(any(Passageiro.class))).thenAnswer(invocation -> invocation.getArgument(0));
        assertDoesNotThrow(() ->{
            desativarPassageiroUseCase.execute(passageiroId);
        });
        verify(repository, times(1)).salvar(any(Passageiro.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar desativar um passageiro que não existe")
    void deveLancarExcecao_QuandoPassageiroNaoExiste() {
        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.empty());
        assertThrows(PassageiroInvalidoException.class, () -> {
            desativarPassageiroUseCase.execute(passageiroId);
        });
        verify(repository, never()).salvar(any(Passageiro.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando passageiro está inativo")
    void deveLancarExcecao_QuandoPassageiroEstaInativo() {
        Passageiro passageiroInativo = passageiroAtivo.desativar();
        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.of(passageiroInativo));
        assertThrows(PassageiroInvalidoException.class, () -> {
            desativarPassageiroUseCase.execute(passageiroId);
        });
        verify(repository, never()).salvar(any(Passageiro.class));
    }
}