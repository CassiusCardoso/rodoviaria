package br.com.rodoviaria.spring_clean_arch.application.usecases.onibus;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.OnibusMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.OnibusInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Placa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuscarInformacoesOnibusUseCaseTest {

    @Mock
    private OnibusRepository onibusRepository;

    @Mock
    private OnibusMapper onibusMapper;

    @InjectMocks
    private BuscarInformacoesOnibusUseCase useCase;

    @Test
    @DisplayName("Deve retornar as informações do ônibus quando o ID existir")
    void deveRetornarInformacoesOnibus_QuandoOnibusExiste() {
        // ARRANGE
        UUID onibusId = UUID.randomUUID();

        Placa placa = new Placa("ABC1234");
        Onibus onibusEncontrado = new Onibus(onibusId, placa, "Mercedes M4", 40, true);

        OnibusResponse responseEsperado = new OnibusResponse(onibusId, "ABC1234", "Mercedes M4", 40, true, LocalDateTime.now());

        // Simula que o repositório encontrou o ônibus
        when(onibusRepository.buscarOnibusPorId(onibusId)).thenReturn(Optional.of(onibusEncontrado));

        // Simula que o mapper converteu a entidade para o DTO de resposta
        when(onibusMapper.toResponse(onibusEncontrado)).thenReturn(responseEsperado);

        // ACT
        OnibusResponse responseReal = useCase.execute(onibusId);

        // ASSERT
        assertNotNull(responseReal);
        assertEquals(responseEsperado.id(), responseReal.id());
        assertEquals(responseEsperado.placa(), responseReal.placa());
        assertEquals(responseEsperado.modelo(), responseReal.modelo());

        verify(onibusRepository).buscarOnibusPorId(onibusId);
        verify(onibusMapper).toResponse(onibusEncontrado);
    }

    @Test
    @DisplayName("Deve lançar OnibusInvalidoException quando o ID não existir")
    void deveLancarExcecao_QuandoOnibusNaoExiste() {
        // ARRANGE
        UUID onibusId = UUID.randomUUID();

        // Simula que o repositório NÃO encontrou o ônibus
        when(onibusRepository.buscarOnibusPorId(onibusId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        OnibusInvalidoException exception = assertThrows(OnibusInvalidoException.class, () -> {
            useCase.execute(onibusId);
        });

        // Verifica a mensagem da exceção, conforme definido no UseCase
        assertEquals("Ônibus com o identificador informado não existe no sistema.", exception.getMessage());

        // Garante que o mapper nunca foi chamado, pois a execução falhou antes
        verify(onibusMapper, never()).toResponse(any(Onibus.class));
    }
}