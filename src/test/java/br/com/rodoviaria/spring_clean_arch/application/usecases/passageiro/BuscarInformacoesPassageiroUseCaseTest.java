package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroResponse;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // HABILITANDO A INTEGRAÇÃO DO MOCKITO COM JUNIT5
public class BuscarInformacoesPassageiroUseCaseTest {
    @Mock
    private PassageiroRepository repository;
    private UUID passageiroId;
    private Passageiro passageiro;
    @InjectMocks
    private BuscarInformacoesPassageiroUseCase buscarInformacoesPassageiroUseCase;

    @BeforeEach
    void setUp(){
        passageiroId = UUID.randomUUID();
        Passageiro passageiroExistente = new Passageiro(
                passageiroId, "Las", new Email("las@gmail.com"), new Senha("12345@g"), new Cpf("389.708.991-20"), new Telefone("(11) 98888-7777"), true
        );
    }

    @Test
    @DisplayName("Deve retornar um passageiro com sucesso quando os dados forem válidos")
    void deveRetornarInformacoesPassageiro_QuandoPassageiroExiste(){

        // ARRRANGE
        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.of(passageiro));

        // ACT - executar o caso de uso
        PassageiroResponse response = buscarInformacoesPassageiroUseCase.execute(passageiroId);

        //
        assertNotNull(response);
        assertEquals(passageiroId, response.id());
        assertEquals("Las", response.nome());
        assertEquals("las@gmail.com", response.email());

        // CONFIRMAR QUE O MÉTODO VERIFY FOI CHAMADO EXATAMENTE UMA VEZ
        verify(repository, times(1)).buscarPassageiroPorId(passageiroId);

    }

    @Test
    @DisplayName("Deve lançar uma exceção quando o passageiro não for encontrado")
    void deveLancarExcecao_QuandoPassageiroNaoExiste(){
        // ASSERT
        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.empty());
        // ACT
        assertThrows(PassageiroInvalidoException.class, () -> {
            buscarInformacoesPassageiroUseCase.execute(passageiroId);
        });
        verify(repository, times(1)).buscarPassageiroPorId(passageiroId);
    }
}
