package br.com.rodoviaria.spring_clean_arch.application.usecases.viagem;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusViagem;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.StatusViagemInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.ViagemInvalidaException;
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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CancelarViagemUseCaseTest {

    @Mock
    private ViagemRepository viagemRepository;

    @InjectMocks
    private CancelarViagemUseCase useCase;

    @Test
    @DisplayName("Deve cancelar viagem com sucesso quando o status for válido")
    void deveCancelarViagemComSucesso_QuandoStatusForValido() {
        // ARRANGE
        UUID viagemId = UUID.randomUUID();
        Viagem viagemAgendada = mock(Viagem.class);
        when(viagemAgendada.getStatusViagem()).thenReturn(StatusViagem.AGENDADA);
        when(viagemRepository.buscarViagemPorId(viagemId)).thenReturn(Optional.of(viagemAgendada));
        when(viagemAgendada.cancelar()).thenReturn(mock(Viagem.class)); // Simula o retorno do método de cancelamento

        // ACT
        useCase.execute(viagemId);

        // ASSERT
        ArgumentCaptor<Viagem> viagemCaptor = ArgumentCaptor.forClass(Viagem.class);
        verify(viagemRepository).salvar(viagemCaptor.capture());
    }

    @ParameterizedTest
    @EnumSource(value = StatusViagem.class, names = {"CANCELADA", "CONCLUIDA", "EM_TRANSITO"})
    @DisplayName("Deve lançar exceção ao tentar cancelar viagem com status inválido")
    void deveLancarExcecao_QuandoStatusDaViagemForInvalido(StatusViagem statusInvalido) {
        // ARRANGE
        UUID viagemId = UUID.randomUUID();
        Viagem viagemComStatusInvalido = mock(Viagem.class);
        when(viagemComStatusInvalido.getStatusViagem()).thenReturn(statusInvalido);
        when(viagemRepository.buscarViagemPorId(viagemId)).thenReturn(Optional.of(viagemComStatusInvalido));

        // ACT & ASSERT
        assertThrows(StatusViagemInvalidoException.class, () -> useCase.execute(viagemId));
        verify(viagemRepository, never()).salvar(any(Viagem.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cancelar viagem que não existe")
    void deveLancarExcecao_QuandoViagemNaoEncontrada() {
        // ARRANGE
        UUID viagemId = UUID.randomUUID();
        when(viagemRepository.buscarViagemPorId(viagemId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ViagemInvalidaException.class, () -> useCase.execute(viagemId));
        verify(viagemRepository, never()).salvar(any(Viagem.class));
    }
}