package br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroPorViagemResponse;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.ViagemInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.TicketRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListarPassageirosDeUmaViagemUseCaseTest {

    @Mock
    private ViagemRepository viagemRepository;
    @Mock
    private TicketRepository ticketRepository;

    private UUID viagemId;
    @InjectMocks
    private ListarPassageirosDeUmaViagemUseCase listarPassageirosDeUmaViagemUseCase;

    @BeforeEach
    void setUp(){
        viagemId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve retornar a lista de passageiros quando uma viagem existe")
    void deveRetornarListaDePassageiros_QuandoViagemExiste(){
        // --- ARRANGE (Organizar) ---

        // Mock para a verificação de existência da viagem:
        // Quando o UseCase chamar buscarViagemPorId, retorne um Optional com um mock de Viagem.
        // Isso confirma que a viagem foi encontrada.
        when(viagemRepository.buscarViagemPorId(viagemId)).thenReturn(Optional.of(mock(Viagem.class)));

        // Mock para a lista de tickets:
        // Crie uma lista simulada de tickets. O conteúdo não importa, apenas a existência e a quantidade.
        // O TicketMapper será chamado, mas como é um singleton estático (MapStruct), ele funciona sem mock.
        List<Ticket> ticketsFalsos = List.of(mock(Ticket.class), mock(Ticket.class));
        when(ticketRepository.listarTicketsPorViagem(viagemId)).thenReturn(ticketsFalsos);

        // ACT
        List<PassageiroPorViagemResponse> resultado = listarPassageirosDeUmaViagemUseCase.execute(viagemId);

        // ASSERT
        assertNotNull(resultado, "A lista de resposta não deve ser nula");
        assertEquals(2, resultado.size(), "A lista de resposta deve conter 2 pessoas.");

        // Verifique as interações com os mocks.
        verify(viagemRepository, times(1)).buscarViagemPorId(viagemId);
        verify(ticketRepository, times(1)).listarTicketsPorViagem(viagemId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a viagem não for encontrada.")
    void deveLancarExcecao_QuandoViagemNaoExiste(){
        // Arrange
        // Simule o cenário onde o repositório não encontra a viagem.
        when(viagemRepository.buscarViagemPorId(viagemId)).thenReturn(Optional.empty());

        // Verifique se a exceção correta é lançada.
        assertThrows(ViagemInvalidaException.class, () -> {
            listarPassageirosDeUmaViagemUseCase.execute(viagemId);
        });
        // --- VERIFY (CORRIGIDO) ---
        // Garanta que o método para buscar a viagem foi chamado.
        verify(viagemRepository, times(1)).buscarViagemPorId(viagemId);

        // Garanta que, como a primeira chamada falhou, a busca por tickets nunca foi realizada.
        verify(ticketRepository, never()).listarTicketsPorViagem(any(UUID.class));
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando a viagem existe, mas não possui tickets")
    void deveRetornarListaVazia_QuandoViagemExisteMasNaoPossuiTickets(){
        // Arrange
        when(viagemRepository.buscarViagemPorId(viagemId)).thenReturn(Optional.of(mock(Viagem.class)));
        when(ticketRepository.listarTicketsPorViagem(viagemId)).thenReturn(Collections.emptyList());

        List<PassageiroPorViagemResponse> resultado = listarPassageirosDeUmaViagemUseCase.execute(viagemId);
        assertNotNull(resultado, "A lista não pode ser nula.");
        assertTrue(resultado.isEmpty(), "A lista deve estar vazia.");

        // VERIFY
        verify(viagemRepository, times(1)).buscarViagemPorId(viagemId);
        verify(ticketRepository, times(1)).listarTicketsPorViagem(viagemId);
    }
}
