package br.com.rodoviaria.spring_clean_arch.application.usecases.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.viagem.AtualizarViagemRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusViagem;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.OnibusInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.StatusViagemInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.ViagemInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
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
public class AtualizarViagemUseCaseTest {

    @Mock
    private ViagemRepository viagemRepository;
    @Mock
    private OnibusRepository onibusRepository;
    @Mock
    private ViagemMapper viagemMapper;

    @InjectMocks
    private AtualizarViagemUseCase useCase;

    @Test
    @DisplayName("Deve atualizar viagem com sucesso quando dados forem válidos")
    void deveAtualizarViagemComSucesso_QuandoDadosValidos() {
        // ARRANGE
        UUID viagemId = UUID.randomUUID();
        UUID novoOnibusId = UUID.randomUUID();
        LocalDateTime novaDataPartida = LocalDateTime.now().plusDays(20);
        AtualizarViagemRequest request = new AtualizarViagemRequest(novaDataPartida, novoOnibusId);

        Linha linhaMock = mock(Linha.class);
        Onibus onibusAtualMock = mock(Onibus.class);
        Onibus novoOnibusMock = mock(Onibus.class);
        Viagem viagemAtual = new Viagem(viagemId, LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(5), StatusViagem.AGENDADA, linhaMock, onibusAtualMock);

        when(viagemRepository.buscarViagemPorId(viagemId)).thenReturn(Optional.of(viagemAtual));
        when(onibusRepository.buscarOnibusPorId(novoOnibusId)).thenReturn(Optional.of(novoOnibusMock));
        // Adicione esta linha no bloco ARRANGE do seu teste de sucesso
        when(viagemRepository.salvar(any(Viagem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(linhaMock.getDuracaoPrevistaMinutos()).thenReturn(300); // 5 horas
        when(viagemMapper.toResponse(any(Viagem.class))).thenReturn(mock(ViagemResponse.class));

        // ACT
        useCase.execute(viagemId, request);

        // ASSERT
        ArgumentCaptor<Viagem> viagemCaptor = ArgumentCaptor.forClass(Viagem.class);
        verify(viagemRepository).salvar(viagemCaptor.capture());
        Viagem viagemSalva = viagemCaptor.getValue();

        assertEquals(novaDataPartida, viagemSalva.getDataPartida());
        assertEquals(novoOnibusMock, viagemSalva.getOnibus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar viagem que não existe")
    void deveLancarExcecao_QuandoViagemNaoEncontrada() {
        // ARRANGE
        UUID viagemId = UUID.randomUUID();
        when(viagemRepository.buscarViagemPorId(viagemId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ViagemInvalidaException.class, () -> useCase.execute(viagemId, mock(AtualizarViagemRequest.class)));
        verify(viagemRepository, never()).salvar(any());
    }

    @ParameterizedTest
    @EnumSource(value = StatusViagem.class, names = {"CONCLUIDA", "CANCELADA", "EM_TRANSITO"})
    @DisplayName("Deve lançar exceção ao tentar atualizar viagem com status inválido")
    void deveLancarExcecao_QuandoStatusDaViagemForInvalido(StatusViagem statusInvalido) {
        // ARRANGE
        UUID viagemId = UUID.randomUUID();
        Viagem viagemComStatusInvalido = mock(Viagem.class);
        when(viagemComStatusInvalido.getStatusViagem()).thenReturn(statusInvalido);
        when(viagemRepository.buscarViagemPorId(viagemId)).thenReturn(Optional.of(viagemComStatusInvalido));

        // ACT & ASSERT
        assertThrows(StatusViagemInvalidoException.class, () -> useCase.execute(viagemId, mock(AtualizarViagemRequest.class)));
        verify(viagemRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o novo ônibus não for encontrado")
    void deveLancarExcecao_QuandoNovoOnibusNaoEncontrado() {
        // ARRANGE
        UUID viagemId = UUID.randomUUID();
        UUID novoOnibusId = UUID.randomUUID();
        AtualizarViagemRequest request = new AtualizarViagemRequest(LocalDateTime.now(), novoOnibusId);

        Viagem viagemAtual = mock(Viagem.class);
        when(viagemAtual.getStatusViagem()).thenReturn(StatusViagem.AGENDADA);
        when(viagemRepository.buscarViagemPorId(viagemId)).thenReturn(Optional.of(viagemAtual));
        when(onibusRepository.buscarOnibusPorId(novoOnibusId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(OnibusInvalidoException.class, () -> useCase.execute(viagemId, request));
        verify(viagemRepository, never()).salvar(any());
    }
}