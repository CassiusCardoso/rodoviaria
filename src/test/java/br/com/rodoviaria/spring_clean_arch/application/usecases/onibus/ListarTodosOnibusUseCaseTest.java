package br.com.rodoviaria.spring_clean_arch.application.usecases.onibus;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.OnibusMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.OnibusRepository;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Placa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListarTodosOnibusUseCaseTest {

    @Mock
    private OnibusRepository onibusRepository;

    @Mock
    private OnibusMapper onibusMapper;

    @InjectMocks
    private ListarTodosOnibusUseCase useCase;

    @Test
    @DisplayName("Deve retornar uma lista de OnibusResponse quando houver ônibus cadastrados")
    void deveRetornarListaDeOnibusResponse_QuandoHouverOnibusCadastrados() {
        // ARRANGE
        // 1. Cria duas entidades de ônibus para simular os dados do repositório
        Onibus onibus1 = new Onibus(UUID.randomUUID(), new Placa("ABC1234"), "Modelo A", 40, true);
        Onibus onibus2 = new Onibus(UUID.randomUUID(), new Placa("DEF5678"), "Modelo B", 42, true);
        List<Onibus> listaDeOnibus = List.of(onibus1, onibus2);

        // 2. Cria os DTOs de resposta esperados
        OnibusResponse response1 = new OnibusResponse(onibus1.getId(), "ABC1234", "Modelo A", 40, true, LocalDateTime.now());
        OnibusResponse response2 = new OnibusResponse(onibus2.getId(), "DEF5678", "Modelo B", 42, true, LocalDateTime.now());

        // 3. Configura os mocks
        // Simula que o repositório retorna a lista de entidades
        when(onibusRepository.listarTodosOnibus()).thenReturn(listaDeOnibus);
        // Simula que o mapper converterá cada entidade para seu respectivo DTO de resposta
        when(onibusMapper.toResponse(onibus1)).thenReturn(response1);
        when(onibusMapper.toResponse(onibus2)).thenReturn(response2);

        // ACT
        List<OnibusResponse> resultado = useCase.execute();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("ABC1234", resultado.get(0).placa());
        assertEquals("DEF5678", resultado.get(1).placa());

        // Verifica se os métodos dos mocks foram chamados como esperado
        verify(onibusRepository, times(1)).listarTodosOnibus();
        verify(onibusMapper, times(2)).toResponse(any(Onibus.class));
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não houver ônibus cadastrados")
    void deveRetornarListaVazia_QuandoNaoHouverOnibusCadastrados() {
        // ARRANGE
        // Simula que o repositório retorna uma lista vazia
        when(onibusRepository.listarTodosOnibus()).thenReturn(Collections.emptyList());

        // ACT
        List<OnibusResponse> resultado = useCase.execute();

        // ASSERT
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        // Verifica se o repositório foi chamado
        verify(onibusRepository, times(1)).listarTodosOnibus();
        // Garante que o mapper nunca foi chamado, pois não havia o que converter
        verify(onibusMapper, never()).toResponse(any(Onibus.class));
    }
}