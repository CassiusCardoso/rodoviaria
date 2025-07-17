package br.com.rodoviaria.spring_clean_arch.application.usecases.viagem;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.viagem.ViagemRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.ViagemMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusViagem;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.OnibusInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.ViagemInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class CriarViagemUseCaseTest {

    @Mock
    private ViagemRepository viagemRepository;
    @Mock
    private LinhaRepository linhaRepository;
    @Mock
    private OnibusRepository onibusRepository;
    @Mock
    private ViagemMapper viagemMapper;

    @InjectMocks
    private CriarViagemUseCase useCase;

    private ViagemRequest request;
    private Linha linha;
    private Onibus onibus;

    @BeforeEach
    void setUp() {
        UUID linhaId = UUID.randomUUID();
        UUID onibusId = UUID.randomUUID();
        request = new ViagemRequest(LocalDateTime.now().plusDays(10), linhaId, onibusId );
        linha = mock(Linha.class);
        onibus = mock(Onibus.class);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar viagem com ônibus já alocado")
    void deveLancarExcecao_QuandoOnibusJaAlocadoEmHorarioConflitante() {
        // ARRANGE
        // Configura o mock da linha para retornar uma duração prevista
        when(linha.getDuracaoPrevistaMinutos()).thenReturn(60); // Supondo 60 minutos de duração
        when(linhaRepository.buscarLinhaPorId(request.linhaId())).thenReturn(Optional.of(linha));
        when(onibusRepository.buscarOnibusPorId(request.onibusId())).thenReturn(Optional.of(onibus));
        when(onibus.getId()).thenReturn(request.onibusId()); // Configura o ID do ônibus
        when(viagemRepository.existeViagemSobrepostaParaOnibus(
                eq(request.onibusId()),
                eq(request.dataPartida()),
                eq(request.dataPartida().plusMinutes(60)) // Corresponde à duração configurada
        )).thenReturn(true);

        // ACT & ASSERT
        assertThrows(OnibusInvalidoException.class, () -> useCase.execute(request));

        // Verifica que o método 'salvar' não foi chamado
        verify(viagemRepository, never()).salvar(any(Viagem.class));
        // Verifica que o método 'existeViagemSobrepostaParaOnibus' foi chamado com os argumentos corretos
        verify(viagemRepository).existeViagemSobrepostaParaOnibus(
                eq(request.onibusId()),
                eq(request.dataPartida()),
                eq(request.dataPartida().plusMinutes(60))
        );
    }

    @Test
    @DisplayName("Deve criar viagem com sucesso quando os dados forem válidos")
    void deveCriarViagemComSucesso_QuandoDadosValidos() {
        // ARRANGE
        when(linhaRepository.buscarLinhaPorId(request.linhaId())).thenReturn(Optional.of(linha));
        when(onibusRepository.buscarOnibusPorId(request.onibusId())).thenReturn(Optional.of(onibus));
        when(viagemRepository.salvar(any(Viagem.class))).thenAnswer(inv -> inv.getArgument(0));
        when(viagemMapper.toResponse(any(Viagem.class))).thenReturn(mock(ViagemResponse.class));

        // ACT
        ViagemResponse response = useCase.execute(request);

        // ASSERT
        assertNotNull(response);

        ArgumentCaptor<Viagem> viagemCaptor = ArgumentCaptor.forClass(Viagem.class);
        verify(viagemRepository).salvar(viagemCaptor.capture());
        Viagem viagemSalva = viagemCaptor.getValue();

        assertEquals(StatusViagem.AGENDADA, viagemSalva.getStatusViagem());
        assertEquals(linha, viagemSalva.getLinha());
        assertEquals(onibus, viagemSalva.getOnibus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar viagem para uma linha que não existe")
    void deveLancarExcecao_QuandoLinhaNaoEncontrada() {
        // ARRANGE
        when(linhaRepository.buscarLinhaPorId(request.linhaId())).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(ViagemInvalidaException.class, () -> useCase.execute(request));
        verify(viagemRepository, never()).salvar(any(Viagem.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar viagem com um ônibus que não existe")
    void deveLancarExcecao_QuandoOnibusNaoEncontrado() {
        // ARRANGE
        when(linhaRepository.buscarLinhaPorId(request.linhaId())).thenReturn(Optional.of(linha));
        when(onibusRepository.buscarOnibusPorId(request.onibusId())).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(OnibusInvalidoException.class, () -> useCase.execute(request));
        verify(viagemRepository, never()).salvar(any(Viagem.class));
    }
}