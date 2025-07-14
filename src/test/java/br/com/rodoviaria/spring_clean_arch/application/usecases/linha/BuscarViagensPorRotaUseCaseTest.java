package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusViagem;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuscarViagensPorRotaUseCaseTest {

    @Mock
    private LinhaRepository linhaRepository;

    @Mock
    private ViagemRepository viagemRepository;

    @Mock
    private ViagemMapper viagemMapper;

    @InjectMocks
    private BuscarViagensPorRotaUseCase useCase;

    @Test
    @DisplayName("Deve retornar uma lista de viagens quando a linha existe e possui viagens")
    void deveRetornarListaDeViagens_QuandoLinhaExisteEContemViagens() {
        // ARRANGE
        UUID linhaId = UUID.randomUUID();
        Linha linhaMock = mock(Linha.class);

        // Mocks de entidades para compor a Viagem
        Onibus onibusMock = mock(Onibus.class);
// Substitua o BigDecimal pelo StatusViagem
        Viagem viagem1 = new Viagem(UUID.randomUUID(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(5), StatusViagem.AGENDADA, linhaMock, onibusMock);
        Viagem viagem2 = new Viagem(UUID.randomUUID(), LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(5), StatusViagem.AGENDADA, linhaMock, onibusMock);
        List<Viagem> listaDeViagens = List.of(viagem1, viagem2);

        // Mocks dos DTOs de resposta
        ViagemResponse response1 = mock(ViagemResponse.class);
        ViagemResponse response2 = mock(ViagemResponse.class);

        // Configuração dos mocks
        when(linhaRepository.buscarLinhaPorId(linhaId)).thenReturn(Optional.of(linhaMock)); //
        when(viagemRepository.buscarViagensPorLinha(linhaId)).thenReturn(listaDeViagens); //
        when(viagemMapper.toResponse(viagem1)).thenReturn(response1); //
        when(viagemMapper.toResponse(viagem2)).thenReturn(response2); //

        // ACT
        List<ViagemResponse> resultado = useCase.execute(linhaId);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(linhaRepository).buscarLinhaPorId(linhaId);
        verify(viagemRepository).buscarViagensPorLinha(linhaId);
        verify(viagemMapper, times(2)).toResponse(any(Viagem.class));
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando a linha existe mas não tem viagens")
    void deveRetornarListaVazia_QuandoLinhaExisteMasNaoContemViagens() {
        // ARRANGE
        UUID linhaId = UUID.randomUUID();
        Linha linhaMock = mock(Linha.class);

        when(linhaRepository.buscarLinhaPorId(linhaId)).thenReturn(Optional.of(linhaMock)); //
        when(viagemRepository.buscarViagensPorLinha(linhaId)).thenReturn(Collections.emptyList()); //

        // ACT
        List<ViagemResponse> resultado = useCase.execute(linhaId);

        // ASSERT
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(linhaRepository).buscarLinhaPorId(linhaId);
        verify(viagemRepository).buscarViagensPorLinha(linhaId);
        verify(viagemMapper, never()).toResponse(any(Viagem.class));
    }

    @Test
    @DisplayName("Deve lançar LinhaInvalidaException quando a linha não existe")
    void deveLancarLinhaInvalidaException_QuandoLinhaNaoExiste() {
        // ARRANGE
        UUID linhaId = UUID.randomUUID();
        when(linhaRepository.buscarLinhaPorId(linhaId)).thenReturn(Optional.empty()); //

        // ACT & ASSERT
        LinhaInvalidaException exception = assertThrows(LinhaInvalidaException.class, () -> {
            useCase.execute(linhaId);
        });

        assertEquals("Linha com o identificador informado não existe.", exception.getMessage()); //
        verify(viagemRepository, never()).buscarViagensPorLinha(any(UUID.class));
        verify(viagemMapper, never()).toResponse(any(Viagem.class));
    }
}