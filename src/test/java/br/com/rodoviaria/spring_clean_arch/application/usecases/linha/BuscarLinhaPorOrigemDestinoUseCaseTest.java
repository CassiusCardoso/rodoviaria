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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuscarLinhaPorOrigemDestinoUseCaseTest {

    @Mock
    private LinhaRepository linhaRepository;

    @Mock
    private LinhaMapper linhaMapper;

    @InjectMocks
    private BuscarLinhaPorOrigemDestinoUseCase useCase;

    @Test
    @DisplayName("Deve retornar LinhaResponse quando a linha com origem e destino existir")
    void deveRetornarLinhaResponse_QuandoLinhaComOrigemEDestinoExistir() {
        // ARRANGE
        String origem = "São Paulo - SP";
        String destino = "Rio de Janeiro - RJ";

        Linha linhaEncontrada = new Linha(UUID.randomUUID(), origem, destino, 360, true);
        LinhaResponse responseEsperado = new LinhaResponse(linhaEncontrada.getId(), origem, destino,360, true, LocalDateTime.now());

        // Configura o comportamento dos mocks
        when(linhaRepository.buscarPorOrigemEDestino(origem, destino)).thenReturn(Optional.of(linhaEncontrada));
        when(linhaMapper.toResponse(linhaEncontrada)).thenReturn(responseEsperado);

        // ACT
        LinhaResponse responseReal = useCase.execute(origem, destino);

        // ASSERT
        assertNotNull(responseReal);
        assertEquals(responseEsperado.id(), responseReal.id());
        assertEquals(origem, responseReal.origem());
        assertEquals(destino, responseReal.destino());

        // Verifica as interações com os mocks
        verify(linhaRepository).buscarPorOrigemEDestino(origem, destino);
        verify(linhaMapper).toResponse(linhaEncontrada);
    }

    @Test
    @DisplayName("Deve lançar LinhaInvalidaException quando a linha não for encontrada")
    void deveLancarLinhaInvalidaException_QuandoLinhaNaoExistir() {
        // ARRANGE
        String origem = "Curitiba - PR";
        String destino = "Florianópolis - SC";

        // Simula que o repositório não encontrou a linha
        when(linhaRepository.buscarPorOrigemEDestino(origem, destino)).thenReturn(Optional.empty());

        // ACT & ASSERT
        // Verifica se a exceção correta é lançada
        LinhaInvalidaException exception = assertThrows(LinhaInvalidaException.class, () -> {
            useCase.execute(origem, destino);
        });

        assertEquals("Não existe nenhuma linha com origem e destino citados.", exception.getMessage());

        // Garante que o mapper nunca foi chamado
        verify(linhaMapper, never()).toResponse(any(Linha.class));
    }
}