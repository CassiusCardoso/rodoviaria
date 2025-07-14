package br.com.rodoviaria.spring_clean_arch.application.usecases.linha;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.LinhaMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Linha;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.LinhaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListarTodasLinhasUseCaseTest {

    @Mock
    private LinhaRepository linhaRepository;

    @Mock
    private LinhaMapper linhaMapper;

    @InjectMocks
    private ListarTodasLinhasUseCase useCase;

    @Test
    @DisplayName("Deve retornar uma lista de LinhaResponse quando houver linhas cadastradas")
    void deveRetornarListaDeLinhas_QuandoExistiremLinhasCadastradas() {
        // ARRANGE
        Linha linha1 = new Linha(UUID.randomUUID(), "Origem A", "Destino A", 60, true);
        Linha linha2 = new Linha(UUID.randomUUID(), "Origem B", "Destino B", 120, true);
        List<Linha> listaDeLinhas = List.of(linha1, linha2);

        // Configura o mock do repositório para retornar a lista
        when(linhaRepository.listarTodasLinhas()).thenReturn(listaDeLinhas);

        // Configura o mock do mapper para simular a conversão
        when(linhaMapper.toResponse(any(Linha.class))).thenAnswer(invocation -> {
            Linha linha = invocation.getArgument(0);
            return new LinhaResponse(linha.getId(), linha.getOrigem(), linha.getDestino(), linha.getDuracaoPrevistaMinutos(), linha.getAtivo(), linha.getCriadoEm());
        });

        // ACT
        List<LinhaResponse> resultado = useCase.execute();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Origem A", resultado.get(0).origem());
        assertEquals("Destino B", resultado.get(1).destino());
        verify(linhaRepository).listarTodasLinhas();
        verify(linhaMapper, times(2)).toResponse(any(Linha.class)); //
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não houver linhas cadastradas")
    void deveRetornarListaVazia_QuandoNaoExistiremLinhas() {
        // ARRANGE
        // Simula que o repositório retorna uma lista vazia
        when(linhaRepository.listarTodasLinhas()).thenReturn(Collections.emptyList());

        // ACT
        List<LinhaResponse> resultado = useCase.execute();

        // ASSERT
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(linhaRepository).listarTodasLinhas();
        verify(linhaMapper, never()).toResponse(any(Linha.class)); // Garante que o mapper não foi chamado
    }
}