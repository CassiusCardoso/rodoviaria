// Conteúdo corrigido para BuscarInformacoesPassageiroUseCaseTest.java
package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.PassageiroMapper;
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

@ExtendWith(MockitoExtension.class)
public class BuscarInformacoesPassageiroUseCaseTest {
    @Mock
    private PassageiroRepository repository;
    @Mock
    private PassageiroMapper passageiroMapper;
    @InjectMocks
    private BuscarInformacoesPassageiroUseCase buscarInformacoesPassageiroUseCase;

    private UUID passageiroId;
    private Passageiro passageiro;

    @BeforeEach
    void setUp() {
        passageiroId = UUID.randomUUID();
        // CORREÇÃO AQUI:
        passageiro = new Passageiro(
                passageiroId, "Las", new Email("las@gmail.com"), Senha.carregar("12345@g"), new Cpf("389.708.991-20"), new Telefone("(11) 98888-7777"), true // <<-- MUDANÇA
        );
    }

    // O resto do arquivo permanece igual
    @Test
    @DisplayName("Deve retornar um passageiro com sucesso quando os dados forem válidos")
    void deveRetornarInformacoesPassageiro_QuandoPassageiroExiste(){
        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.of(passageiro));
        PassageiroResponse responseFalso = new PassageiroResponse(passageiroId, "John Doe", "john.doe@gmail.com", "111.222.333-44", "(11) 98888-7777", true, null);
        when(passageiroMapper.toResponse(any(Passageiro.class))).thenReturn(responseFalso);
        PassageiroResponse responseReal = buscarInformacoesPassageiroUseCase.execute(passageiroId);
        assertNotNull(responseReal);
        assertEquals("John Doe", responseReal.nome());
        verify(repository, times(1)).buscarPassageiroPorId(passageiroId);
        verify(passageiroMapper, times(1)).toResponse(any(Passageiro.class));
    }

    @Test
    @DisplayName("Deve lançar uma exceção quando o passageiro não for encontrado")
    void deveLancarExcecao_QuandoPassageiroNaoExiste(){
        when(repository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.empty());
        assertThrows(PassageiroInvalidoException.class, () -> {
            buscarInformacoesPassageiroUseCase.execute(passageiroId);
        });
        verify(repository, times(1)).buscarPassageiroPorId(passageiroId);
    }
}