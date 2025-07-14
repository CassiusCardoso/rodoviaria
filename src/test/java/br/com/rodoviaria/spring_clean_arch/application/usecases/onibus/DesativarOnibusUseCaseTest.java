package br.com.rodoviaria.spring_clean_arch.application.usecases.onibus;

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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DesativarOnibusUseCaseTest {

    @Mock
    private OnibusRepository onibusRepository;

    @Mock
    private ViagemRepository viagemRepository;

    @InjectMocks
    private DesativarOnibusUseCase useCase;

    private Onibus onibusAtivo;
    private UUID onibusId;

    @BeforeEach
    void setUp() {
        onibusId = UUID.randomUUID();
        Placa placa = new Placa("ABC1234");
        // Por padrão, criamos um ônibus ativo para os testes
        onibusAtivo = new Onibus(
                onibusId,
                placa,
                "Mercedes M3",
                40,
                true
        );
    }

    @Test
    @DisplayName("Deve desativar um ônibus com sucesso quando ele for válido para desativação")
    void deveDesativarOnibusComSucesso_QuandoOnibusValido() {
        // ARRANGE
        // Simula que o repositório encontrou o ônibus ativo
        when(onibusRepository.buscarOnibusPorId(onibusId)).thenReturn(Optional.of(onibusAtivo));
        // Simula que não existem viagens futuras para este ônibus
        when(viagemRepository.existeViagemFuturaNaoCanceladaParaOnibus(onibusId)).thenReturn(false);

        // ACT
        useCase.execute(onibusId);

        // ASSERT
        // Captura o objeto Onibus que foi passado para o método 'salvar'
        ArgumentCaptor<Onibus> onibusCaptor = ArgumentCaptor.forClass(Onibus.class);
        verify(onibusRepository).salvar(onibusCaptor.capture());
        Onibus onibusSalvo = onibusCaptor.getValue();

        // Garante que o ônibus salvo está de fato inativo
        assertFalse(onibusSalvo.getAtivo());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar desativar um ônibus que não existe")
    void deveLancarExcecao_QuandoOnibusNaoEncontrado() {
        // ARRANGE
        // Simula que o repositório não encontrou o ônibus
        when(onibusRepository.buscarOnibusPorId(onibusId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        OnibusInvalidoException exception = assertThrows(OnibusInvalidoException.class, () -> {
            useCase.execute(onibusId);
        });

        assertEquals("Ônibus com o identificador informado não existe.", exception.getMessage()); //
        verify(onibusRepository, never()).salvar(any(Onibus.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar desativar um ônibus que já está inativo")
    void deveLancarExcecao_QuandoOnibusJaEstiverInativo() {
        // ARRANGE
        Placa placa = new Placa("DEF5678");
        Onibus onibusJaInativo = new Onibus(onibusId, placa, "Volvo V2", 38, false);
        // Simula que o repositório encontrou um ônibus já inativo
        when(onibusRepository.buscarOnibusPorId(onibusId)).thenReturn(Optional.of(onibusJaInativo));

        // ACT & ASSERT
        OnibusInvalidoException exception = assertThrows(OnibusInvalidoException.class, () -> {
            useCase.execute(onibusId);
        });

        assertEquals("Ônibus inválido para desativar.", exception.getMessage()); //
        verify(onibusRepository, never()).salvar(any(Onibus.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar desativar um ônibus com viagens futuras")
    void deveLancarExcecao_QuandoOnibusTemViagensFuturas() {
        // ARRANGE
        // Simula que o repositório encontrou o ônibus ativo
        when(onibusRepository.buscarOnibusPorId(onibusId)).thenReturn(Optional.of(onibusAtivo));
        // Simula que existem viagens futuras para este ônibus
        when(viagemRepository.existeViagemFuturaNaoCanceladaParaOnibus(onibusId)).thenReturn(true);

        // ACT & ASSERT
        OnibusInvalidoException exception = assertThrows(OnibusInvalidoException.class, () -> {
            useCase.execute(onibusId);
        });

        assertEquals("Não é possível desativar um ônibus que está alocado em viagens futuras. Cancele ou realoque as viagens primeiro.", exception.getMessage()); //
        verify(onibusRepository, never()).salvar(any(Onibus.class));
    }
}
