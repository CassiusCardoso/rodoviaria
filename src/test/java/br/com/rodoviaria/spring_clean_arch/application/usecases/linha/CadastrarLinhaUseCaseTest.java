package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.linha.CadastrarLinhaRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.LinhaMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.LinhaInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;
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
public class CadastrarLinhaUseCaseTest {

    @Mock
    private LinhaRepository linhaRepository;

    @Mock
    private LinhaMapper linhaMapper;

    @InjectMocks
    private CadastrarLinhaUseCase useCase;

    private CadastrarLinhaRequest request;

    @BeforeEach
    void setUp() {
        request = new CadastrarLinhaRequest("São Paulo - SP", "Rio de Janeiro - RJ", 360);
    }

    @Test
    @DisplayName("Deve cadastrar linha com sucesso quando não houver duplicidade")
    void deveCadastrarLinhaComSucesso_QuandoNaoHouverDuplicidade() {
        // ARRANGE
        // Simula que não existe linha com a mesma origem e destino
        when(linhaRepository.buscarPorOrigemEDestino(request.origem(), request.destino())).thenReturn(Optional.empty());

        // Simula a ação de salvar, retornando a própria entidade salva
        when(linhaRepository.salvar(any(Linha.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Simula a resposta do mapper
        LinhaResponse responseEsperado = new LinhaResponse(UUID.randomUUID(), request.origem(), request.destino(), request.duracaoPrevistaMinutos(), true, LocalDateTime.now());
        when(linhaMapper.toResponse(any(Linha.class))).thenReturn(responseEsperado);

        // ACT
        LinhaResponse responseReal = useCase.execute(request);

        // ASSERT
        assertNotNull(responseReal);
        assertEquals(request.origem(), responseReal.origem());
        assertEquals(request.destino(), responseReal.destino());

        ArgumentCaptor<Linha> linhaCaptor = ArgumentCaptor.forClass(Linha.class);
        verify(linhaRepository).salvar(linhaCaptor.capture());
        Linha linhaSalva = linhaCaptor.getValue();

        assertTrue(linhaSalva.getAtivo()); // Garante que a linha foi criada como ativa
    }

    @Test
    @DisplayName("Deve lançar exceção quando linha com mesma origem e destino já existir")
    void deveLancarExcecao_QuandoLinhaComMesmaOrigemEDestinoJaExistir() {
        // ARRANGE
        // Simula que uma linha com a mesma origem e destino já foi encontrada
        when(linhaRepository.buscarPorOrigemEDestino(request.origem(), request.destino())).thenReturn(Optional.of(mock(Linha.class)));

        // ACT & ASSERT
        LinhaInvalidaException exception = assertThrows(LinhaInvalidaException.class, () -> {
            useCase.execute(request);
        });

        assertEquals("Já existe uma linha cadastrada com esta mesma origem e destino.", exception.getMessage()); //
        verify(linhaRepository, never()).salvar(any(Linha.class));
    }
}