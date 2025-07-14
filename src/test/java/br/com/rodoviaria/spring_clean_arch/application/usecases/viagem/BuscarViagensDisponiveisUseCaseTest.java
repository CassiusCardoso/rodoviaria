package br.com.rodoviaria.spring_clean_arch.application.usecases.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.viagem.BuscarViagensDisponiveisRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagensDisponiveisResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuscarViagensDisponiveisUseCaseTest {

    @Mock
    private ViagemRepository viagemRepository;
    @Mock
    private ViagemMapper viagemMapper;

    @InjectMocks
    private BuscarViagensDisponiveisUseCase useCase;

    @Test
    @DisplayName("Deve retornar viagens disponíveis quando elas forem encontradas")
    void deveRetornarViagensDisponiveis_QuandoViagensSaoEncontradas() {
        // ARRANGE
        BuscarViagensDisponiveisRequest request = new BuscarViagensDisponiveisRequest(LocalDateTime.now(), "Origem", "Destino");
        Viagem viagemMock = mock(Viagem.class);
        List<Viagem> listaDeViagens = List.of(viagemMock);

        when(viagemRepository.buscarPorDataOrigemDestino(request.data(), request.origem(), request.destino())).thenReturn(listaDeViagens);
        when(viagemMapper.toViagensDisponiveisResponse(any(Viagem.class))).thenReturn(mock(ViagensDisponiveisResponse.class));

        // ACT
        List<ViagensDisponiveisResponse> resultado = useCase.execute(request);

        // ASSERT
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(viagemRepository).buscarPorDataOrigemDestino(request.data(), request.origem(), request.destino());
        verify(viagemMapper).toViagensDisponiveisResponse(viagemMock);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhuma viagem for encontrada")
    void deveRetornarListaVazia_QuandoNenhumaViagemEhEncontrada() {
        // ARRANGE
        BuscarViagensDisponiveisRequest request = new BuscarViagensDisponiveisRequest(LocalDateTime.now(), "Origem", "Destino");
        when(viagemRepository.buscarPorDataOrigemDestino(request.data(), request.origem(), request.destino())).thenReturn(Collections.emptyList());

        // ACT
        List<ViagensDisponiveisResponse> resultado = useCase.execute(request);

        // ASSERT
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(viagemMapper, never()).toViagensDisponiveisResponse(any());
    }
}