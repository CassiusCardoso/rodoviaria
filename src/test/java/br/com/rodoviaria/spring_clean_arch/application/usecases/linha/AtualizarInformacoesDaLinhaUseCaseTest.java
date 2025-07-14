package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.linha.AtualizarInformacoesDaLinhaRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.LinhaMapper;
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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AtualizarInformacoesDaLinhaUseCaseTest {

    @Mock
    private LinhaRepository linhaRepository;

    @Mock
    private LinhaMapper linhaMapper;

    @InjectMocks
    private AtualizarInformacoesDaLinhaUseCase useCase;

    @Test
    @DisplayName("Deve atualizar as informações da linha com sucesso")
    void deveAtualizarInformacoesDaLinhaComSucesso() {
        // ARRANGE
        UUID linhaId = UUID.randomUUID();
        AtualizarInformacoesDaLinhaRequest request = new AtualizarInformacoesDaLinhaRequest("Rio de Janeiro - RJ", "São Paulo - SP", 360);

        // Cria uma linha existente com dados antigos
        Linha linhaExistente = new Linha(linhaId, "Niterói - RJ", "Cabo Frio - RJ", 120, true);

        // Cria a resposta esperada com os dados novos
        LinhaResponse responseEsperado = new LinhaResponse(linhaId, "Rio de Janeiro - RJ", "São Paulo - SP", 360, true, LocalDateTime.now());

        // Configura os mocks
        when(linhaRepository.buscarLinhaPorId(linhaId)).thenReturn(Optional.of(linhaExistente));
        when(linhaRepository.salvar(any(Linha.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(linhaMapper.toResponse(any(Linha.class))).thenReturn(responseEsperado);

        // ACT
        LinhaResponse responseReal = useCase.execute(linhaId, request);

        // ASSERT
        // Verifica se a resposta do UseCase está correta
        assertNotNull(responseReal);
        assertEquals(responseEsperado.id(), responseReal.id());
        assertEquals(responseEsperado.origem(), responseReal.origem());
        assertEquals(responseEsperado.destino(), responseReal.destino());

        // Captura e verifica a entidade que foi salva
        ArgumentCaptor<Linha> linhaCaptor = ArgumentCaptor.forClass(Linha.class);
        verify(linhaRepository).salvar(linhaCaptor.capture());
        Linha linhaSalva = linhaCaptor.getValue();

        assertEquals(request.origem(), linhaSalva.getOrigem());
        assertEquals(request.destino(), linhaSalva.getDestino());
        assertEquals(request.duracaoPrevistaMinutos(), linhaSalva.getDuracaoPrevistaMinutos());
        assertTrue(linhaSalva.getAtivo()); // Garante que o status 'ativo' foi mantido
    }

    @Test
    @DisplayName("Deve lançar LinhaInvalidaException quando a linha não for encontrada")
    void deveLancarLinhaInvalidaException_QuandoLinhaNaoEncontrada() {
        // ARRANGE
        UUID linhaId = UUID.randomUUID();
        AtualizarInformacoesDaLinhaRequest request = new AtualizarInformacoesDaLinhaRequest("Origem", "Destino", 60);

        // Simula que o repositório não encontrou a linha
        when(linhaRepository.buscarLinhaPorId(linhaId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        // Verifica se a exceção correta é lançada
        LinhaInvalidaException exception = assertThrows(LinhaInvalidaException.class, () -> {
            useCase.execute(linhaId, request);
        });

        assertTrue(exception.getMessage().contains("Não existe nenhuma linha registrada com o identificador")); //

        // Garante que o método 'salvar' nunca foi chamado
        verify(linhaRepository, never()).salvar(any(Linha.class));
    }
}
