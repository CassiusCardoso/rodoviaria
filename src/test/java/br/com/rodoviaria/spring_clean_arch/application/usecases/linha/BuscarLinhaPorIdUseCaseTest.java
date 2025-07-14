package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.LinhaMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;
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
public class BuscarLinhaPorIdUseCaseTest {

    @Mock
    private LinhaRepository linhaRepository;

    @Mock
    private LinhaMapper linhaMapper;

    @InjectMocks
    private BuscarLinhaPorIdUseCase useCase;

    @Test
    @DisplayName("Deve retornar LinhaResponse quando a linha for encontrada")
    void deveRetornarLinhaResponse_QuandoLinhaExistir() {
        // ARRANGE
        UUID linhaId = UUID.randomUUID();
        Linha linhaEncontrada = new Linha(linhaId, "Origem Teste", "Destino Teste", 120, true);
        LinhaResponse responseEsperado = new LinhaResponse(linhaId, "Origem Teste", "Destino Teste", 120, true, LocalDateTime.now());

        // Simula o comportamento do repositório
        when(linhaRepository.buscarLinhaPorId(linhaId)).thenReturn(Optional.of(linhaEncontrada));

        // Simula o comportamento do mapper
        when(linhaMapper.toResponse(linhaEncontrada)).thenReturn(responseEsperado);

        // ACT
        LinhaResponse responseReal = useCase.execute(linhaId);

        // ASSERT
        assertNotNull(responseReal);
        assertEquals(responseEsperado.id(), responseReal.id());
        assertEquals(responseEsperado.origem(), responseReal.origem());
        assertEquals(responseEsperado.destino(), responseReal.destino());

        // Verifica se os mocks foram chamados
        verify(linhaRepository).buscarLinhaPorId(linhaId);
        verify(linhaMapper).toResponse(linhaEncontrada);
    }

    @Test
    @DisplayName("Deve lançar LinhaInvalidaException quando a linha não for encontrada")
    void deveLancarLinhaInvalidaException_QuandoLinhaNaoExistir() {
        // ARRANGE
        UUID linhaId = UUID.randomUUID();

        // Simula que o repositório não encontrou a linha
        when(linhaRepository.buscarLinhaPorId(linhaId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        LinhaInvalidaException exception = assertThrows(LinhaInvalidaException.class, () -> {
            useCase.execute(linhaId);
        });

        assertEquals("Linha não encontrada.", exception.getMessage()); //

        // Garante que o mapper nunca foi chamado, pois a execução falhou antes
        verify(linhaMapper, never()).toResponse(any(Linha.class));
    }
}
