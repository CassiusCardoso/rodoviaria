package br.com.rodoviaria.spring_clean_arch.application.usecases.ticket;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.TicketMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket.TicketInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListarMeusTicketsUseCaseTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private TicketMapper ticketMapper;

    private UUID ticketId;
    private UUID viagemId;
    private UUID passageiroId;
    private Ticket ticketMock;
    private Passageiro passageiroMock;
    @InjectMocks
    private ListarMeusTicketsUseCase listarMeusTicketsUseCase;

    @BeforeEach
    void setUp() {
        UUID ticketId = UUID.randomUUID();
        UUID viagemId = UUID.randomUUID();
        passageiroId = UUID.randomUUID();

        passageiroMock = mock(Passageiro.class);
    }

    @Test
    @DisplayName("Deve retornar tickets quando existirem.")
    void deveRetornarListaDeTickets_QuandoExistirem() {
        // ARRANGE
        List<Ticket> ticketsFalsos = List.of(mock(Ticket.class), mock(Ticket.class));

        // Configure o mock para o método correto: listarTicketsPorPassageiroId.
        when(ticketRepository.listarTicketsPorPassageiroId(passageiroId)).thenReturn(ticketsFalsos);

        // ACT
        List<TicketResponse> resultado = listarMeusTicketsUseCase.execute(passageiroId);

        // ASSERT
        assertNotNull(resultado, "A lista de resposta não deve ser nula.");
        assertEquals(2, resultado.size(), "A lista de resposta deve conter 2 tickets.");

        // --- VERIFY (CORRIGIDO) ---
        // Verifique se o método que REALMENTE é chamado no UseCase foi invocado.
        verify(ticketRepository, times(1)).listarTicketsPorPassageiroId(passageiroId);

        // A verificação do mapper estava chamando toResponse(ticketMock), mas ticketMock não existe mais
        // A verificação correta é garantir que o mapper foi chamado 2 vezes (uma para cada item da lista)
        verify(ticketMapper, times(2)).toResponse(any(Ticket.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando passageiro não tiver tickets.")
    void deveRetornarListaVazia_QuandoPassageiroNaoTiverTickets() {
        // ARRANGE
        when(ticketRepository.listarTicketsPorPassageiroId(passageiroId)).thenReturn(Collections.emptyList());

        // PARA UM CASO DE LISTAS, FAZ MAIS SENTIDO RETORNAR A LISTA VAZIA DO QUE LANÇAR EXCEÇÃO
        List<TicketResponse> resultado = listarMeusTicketsUseCase.execute(passageiroId);

        // ASSERT
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "A lista de resposta deve estar vazia.");

        // VERIFY
        verify(ticketRepository, times(1)).listarTicketsPorPassageiroId(passageiroId);

        // Verify
        verify(ticketMapper, never()).toResponse(any(Ticket.class));

    }

    @Test
    @DisplayName("Deve lançar exceção quando o ID do passageiro for nulo.")
    void deveLancarExcecao_QuandoIdDoPassageiroForNulo() {
        // ARRANGE - Não é necessário configurar mocks, pois a execução deve parar antes.

        // ACT & ASSERT
        // A asserção já está correta, esperando a exceção.
        assertThrows(IllegalArgumentException.class, () -> {
            listarMeusTicketsUseCase.execute(null);
        });

        // VERIFY
        // Garante que nenhuma interação com o repositório ou mapper ocorreu.
        verifyNoInteractions(ticketRepository);
        verifyNoInteractions(ticketMapper);

    }
}
