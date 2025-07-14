package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;
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
public class DesativarLinhaUseCaseTest {

    @Mock
    private LinhaRepository linhaRepository;

    @InjectMocks
    private DesativarLinhaUseCase useCase;

    @Test
    @DisplayName("Deve desativar uma linha com sucesso quando ela existir")
    void deveDesativarLinhaComSucesso_QuandoLinhaExiste() {
        // ARRANGE
        UUID linhaId = UUID.randomUUID();
        Linha linhaAtiva = new Linha(linhaId, "Origem", "Destino", 120, true);

        // Simula que a linha foi encontrada
        when(linhaRepository.buscarLinhaPorId(linhaId)).thenReturn(Optional.of(linhaAtiva));

        // ACT
        useCase.execute(linhaId);

        // ASSERT
        // Captura a entidade que foi passada para o método 'salvar'
        ArgumentCaptor<Linha> linhaCaptor = ArgumentCaptor.forClass(Linha.class);
        verify(linhaRepository).salvar(linhaCaptor.capture());
        Linha linhaSalva = linhaCaptor.getValue();

        // Garante que a linha salva está de fato inativa
        assertFalse(linhaSalva.getAtivo());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar desativar uma linha que não existe")
    void deveLancarExcecao_QuandoLinhaNaoExiste() {
        // ARRANGE
        UUID linhaId = UUID.randomUUID();
        // Simula que a linha não foi encontrada
        when(linhaRepository.buscarLinhaPorId(linhaId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        LinhaInvalidaException exception = assertThrows(LinhaInvalidaException.class, () -> {
            useCase.execute(linhaId);
        });

        assertEquals("A linha com o número identificador não existe no sistema.", exception.getMessage()); //
        verify(linhaRepository, never()).salvar(any(Linha.class));
    }
}