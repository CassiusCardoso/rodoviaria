package br.com.rodoviaria.spring_clean_arch.application.usecases.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemPorPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.PassageiroInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
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
public class ListarViagensPorPassageiroUseCaseTest {

    @Mock
    private ViagemRepository viagemRepository;
    @Mock
    private PassageiroRepository passageiroRepository;
    @Mock
    private ViagemMapper viagemMapper;

    @InjectMocks
    private ListarViagensPorPassageiroUseCase useCase;

    @Test
    @DisplayName("Deve retornar lista de viagens quando passageiro existe e possui tickets")
    void deveRetornarListaDeViagens_QuandoPassageiroExisteEComprouTickets() {
        // ARRANGE
        UUID passageiroId = UUID.randomUUID();
        Passageiro passageiroMock = mock(Passageiro.class);
        Viagem viagemMock = mock(Viagem.class);
        List<Viagem> listaDeViagens = List.of(viagemMock);
        ViagemPorPassageiroResponse responseMock = mock(ViagemPorPassageiroResponse.class);

        when(passageiroRepository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.of(passageiroMock));
        when(viagemRepository.buscarViagensPorPassageiro(passageiroId)).thenReturn(listaDeViagens);
        when(viagemMapper.toViagemPorPassageiroResponse(viagemMock)).thenReturn(responseMock);

        // ACT
        List<ViagemPorPassageiroResponse> resultado = useCase.execute(passageiroId);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(passageiroRepository).buscarPassageiroPorId(passageiroId);
        verify(viagemRepository).buscarViagensPorPassageiro(passageiroId);
        verify(viagemMapper).toViagemPorPassageiroResponse(viagemMock);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando passageiro existe mas não possui tickets")
    void deveRetornarListaVazia_QuandoPassageiroExisteMasNaoComprouTickets() {
        // ARRANGE
        UUID passageiroId = UUID.randomUUID();
        Passageiro passageiroMock = mock(Passageiro.class);

        when(passageiroRepository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.of(passageiroMock));
        when(viagemRepository.buscarViagensPorPassageiro(passageiroId)).thenReturn(Collections.emptyList());

        // ACT
        List<ViagemPorPassageiroResponse> resultado = useCase.execute(passageiroId);

        // ASSERT
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(viagemMapper, never()).toViagemPorPassageiroResponse(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o passageiro não existe")
    void deveLancarExcecao_QuandoPassageiroNaoExiste() {
        // ARRANGE
        UUID passageiroId = UUID.randomUUID();
        when(passageiroRepository.buscarPassageiroPorId(passageiroId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(PassageiroInvalidoException.class, () -> useCase.execute(passageiroId));
        verify(viagemRepository, never()).buscarViagensPorPassageiro(any());
    }
}