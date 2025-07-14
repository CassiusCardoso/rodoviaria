
package br.com.rodoviaria.spring_clean_arch.application.usecases.onibus;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.onibus.AtualizarOnibusRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.OnibusMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus.OnibusInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Placa;
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
public class AtualizarOnibusUseCaseTest {

    @Mock
    private OnibusRepository onibusRepository;

    @Mock
    private ViagemRepository viagemRepository;

    @Mock
    private OnibusMapper onibusMapper;

    private UUID onibusId;

    private Onibus onibus;


    @InjectMocks
    private AtualizarOnibusUseCase atualizarOnibusUseCase;

    @BeforeEach
    void setUp(){
        onibusId = UUID.randomUUID();

        // Corrigido para usar o construtor correto da entidade Onibus com o Value Object Placa
        Placa placa = new Placa("ABC1234");
        onibus = new Onibus(
                onibusId,
                placa,
                "Mercedes M3",
                10,
                true
        );
    }

    @Test
    @DisplayName("Deve atualizar ônibus com sucesso quando os dados são válidos")
    void deveAtualizarOnibusComSucesso_QuandoDadosValidos() {
        // ARRANGE
        AtualizarOnibusRequest request = new AtualizarOnibusRequest("Modelo Novo", 50);

        when(onibusRepository.buscarOnibusPorId(onibusId)).thenReturn(Optional.of(onibus)); //
        when(viagemRepository.existeViagemEmTransitoParaOnibus(onibusId)).thenReturn(false); //
        when(onibusRepository.salvar(any(Onibus.class))).thenAnswer(invocation -> invocation.getArgument(0)); //

        OnibusResponse responseEsperado = new OnibusResponse(onibusId, "ABC1234", "Modelo Novo", 50, true, LocalDateTime.now());
        when(onibusMapper.toResponse(any(Onibus.class))).thenReturn(responseEsperado); //

        // ACT
        OnibusResponse responseReal = atualizarOnibusUseCase.execute(request, onibusId);

        // ASSERT
        assertNotNull(responseReal);
        assertEquals("Modelo Novo", responseReal.modelo());
        assertEquals(50, responseReal.capacidade());

        ArgumentCaptor<Onibus> onibusCaptor = ArgumentCaptor.forClass(Onibus.class);
        verify(onibusRepository).salvar(onibusCaptor.capture());
        Onibus onibusSalvo = onibusCaptor.getValue();

        assertEquals("Modelo Novo", onibusSalvo.getModelo());
        assertEquals(50, onibusSalvo.getCapacidade());
        assertEquals("ABC1234", onibusSalvo.getPlaca().getValor());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o ônibus não for encontrado")
    void deveLancarExcecao_QuandoOnibusNaoEncontrado() {
        // ARRANGE
        AtualizarOnibusRequest request = new AtualizarOnibusRequest("Modelo Novo", 50);
        when(onibusRepository.buscarOnibusPorId(onibusId)).thenReturn(Optional.empty()); //

        // ACT & ASSERT
        OnibusInvalidoException exception = assertThrows(OnibusInvalidoException.class, () -> {
            atualizarOnibusUseCase.execute(request, onibusId);
        });

        assertEquals("Não há nenhum registro de ônibus com esse identificador", exception.getMessage()); //
        verify(onibusRepository, never()).salvar(any(Onibus.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar alterar ônibus desativado")
    void deveLancarExcecao_QuandoOnibusEstiverDesativado() {
        // ARRANGE
        AtualizarOnibusRequest request = new AtualizarOnibusRequest("Modelo Novo", 50);

        Placa placa = new Placa("DEF5678");
        Onibus onibusInativo = new Onibus(onibusId, placa, "Modelo Antigo", 30, false);
        when(onibusRepository.buscarOnibusPorId(onibusId)).thenReturn(Optional.of(onibusInativo));

        // ACT & ASSERT
        OnibusInvalidoException exception = assertThrows(OnibusInvalidoException.class, () -> {
            atualizarOnibusUseCase.execute(request, onibusId);
        });

        assertEquals("Não é possível alterar um ônibus que já está desativado.", exception.getMessage()); //
        verify(onibusRepository, never()).salvar(any(Onibus.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar alterar ônibus em trânsito")
    void deveLancarExcecao_QuandoOnibusEstiverEmTransito() {
        // ARRANGE
        AtualizarOnibusRequest request = new AtualizarOnibusRequest("Modelo Novo", 50);
        when(onibusRepository.buscarOnibusPorId(onibusId)).thenReturn(Optional.of(onibus));
        when(viagemRepository.existeViagemEmTransitoParaOnibus(onibusId)).thenReturn(true); //

        // ACT & ASSERT
        OnibusInvalidoException exception = assertThrows(OnibusInvalidoException.class, () -> {
            atualizarOnibusUseCase.execute(request, onibusId);
        });

        assertEquals("Não é possível alterar um ônibus que está atualmente em trânsito.", exception.getMessage()); //
        verify(onibusRepository, never()).salvar(any(Onibus.class));
    }
}
