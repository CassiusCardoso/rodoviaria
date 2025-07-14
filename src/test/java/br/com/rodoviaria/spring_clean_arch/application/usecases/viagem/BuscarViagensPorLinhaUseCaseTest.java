package br.com.rodoviaria.spring_clean_arch.application.usecases.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemPorLinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuscarViagensPorLinhaUseCaseTest {

    @Mock
    private ViagemRepository viagemRepository;
    @Mock
    private LinhaRepository linhaRepository;
    @Mock
    private ViagemMapper viagemMapper;

    @InjectMocks
    private BuscarViagensPorLinhaUseCase useCase;

    @Test
    @DisplayName("Deve retornar lista de viagens quando a linha existe e possui viagens")
    void deveRetornarListaDeViagens_QuandoLinhaExisteEHaViagens() {
        // ARRANGE
        UUID linhaId = UUID.randomUUID();
        Linha linhaMock = mock(Linha.class);
        Viagem viagemMock = mock(Viagem.class);
        List<Viagem> listaDeViagens = List.of(viagemMock);

        when(linhaRepository.buscarLinhaPorId(linhaId)).thenReturn(Optional.of(linhaMock));
        when(viagemRepository.buscarViagensPorLinha(linhaId)).thenReturn(listaDeViagens);
        when(viagemMapper.toViagemPorLinhaResponse(any(Viagem.class))).thenReturn(mock(ViagemPorLinhaResponse.class));

        // ACT
        List<ViagemPorLinhaResponse> resultado = useCase.execute(linhaId);

        // ASSERT
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(viagemMapper).toViagemPorLinhaResponse(viagemMock);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando a linha existe mas não possui viagens")
    void deveRetornarListaVazia_QuandoLinhaExisteMasNaoHaViagens() {
        // ARRANGE
        UUID linhaId = UUID.randomUUID();
        Linha linhaMock = mock(Linha.class);

        when(linhaRepository.buscarLinhaPorId(linhaId)).thenReturn(Optional.of(linhaMock));
        when(viagemRepository.buscarViagensPorLinha(linhaId)).thenReturn(Collections.emptyList());

        // ACT
        List<ViagemPorLinhaResponse> resultado = useCase.execute(linhaId);

        // ASSERT
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(viagemMapper, never()).toViagemPorLinhaResponse(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a linha não existe")
    void deveLancarExcecao_QuandoLinhaNaoExiste() {
        // ARRANGE
        UUID linhaId = UUID.randomUUID();
        when(linhaRepository.buscarLinhaPorId(linhaId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(LinhaInvalidaException.class, () -> useCase.execute(linhaId));
        verify(viagemRepository, never()).buscarViagensPorLinha(any());
    }
}