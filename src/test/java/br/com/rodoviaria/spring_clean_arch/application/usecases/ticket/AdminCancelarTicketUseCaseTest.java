package br.com.rodoviaria.spring_clean_arch.application.usecases.ticket;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.enums.FormaPagamento;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusTicket;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket.TicketInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminCancelarTicketUseCaseTest {

    @Mock
    private TicketRepository repository;

    private UUID ticketId;

    private Ticket ticket;

    @Mock
    private Viagem viagemMock;
    @Mock
    private Passageiro passageiroMock;
    @Mock
    private Onibus onibusMock;

    @InjectMocks
    private AdminCancelarTicketUseCase useCase;

    @BeforeEach
    void setUp(){
        ticketId = UUID.randomUUID();
        viagemMock = mock(Viagem.class);
        passageiroMock = mock(Passageiro.class);
        onibusMock = mock(Onibus.class);

        // Ensine os mocks a se comportarem em cadeia:
        // 1. Quando chamarem getOnibus() na viagem, retorne o mock do ônibus.
        when(viagemMock.getOnibus()).thenReturn(onibusMock);
        // 2. Quando chamarem getCapacidade() no ônibus, retorne um valor qualquer (ex: 40).
        when(onibusMock.getCapacidade()).thenReturn(40);
        // 3. A data de partida também é necessária para o construtor do Ticket.
        when(viagemMock.getDataPartida()).thenReturn(LocalDateTime.now().plusDays(1));

        ticket = new Ticket(
                ticketId,
                "Nome",
                "123",
                10,
                new BigDecimal(100),
                FormaPagamento.PIX,
                StatusTicket.CONFIRMADO,
                passageiroMock,
                viagemMock
        );
    }
    @Test
    @DisplayName("Admin deve cancelar ticket com sucesso.")
    void deveRetornarTicketCancelado_QuandoAdminForValido(){
        // ARRANGE
        when(repository.buscarTicketPorId(ticketId)).thenReturn(Optional.of(ticket));

        // ACT
        useCase.execute(ticketId);

        // ASSERT
        // Captura o argumento que foi passado para o método salvar
        ArgumentCaptor<Ticket> ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(repository).salvar(ticketArgumentCaptor.capture());

        // Pegar o ticket capturado
        Ticket ticketSalvo = ticketArgumentCaptor.getValue();

        // Verifica se o status do ticket salvo é 'CANCELADO'
        assertEquals(StatusTicket.CANCELADO, ticketSalvo.getStatus());

    }

    @Test
    @DisplayName("Deve lançar exceção quando tentar comprar um ticket inexistente")
    void deveLancarExcecao_QuandoTicketNaoEncontrado(){
        // ARRANGE
        when(repository.buscarTicketPorId(ticketId)).thenReturn(Optional.empty());

        // VERIFICA SE A EXCEÇÃO LANÇADA É A CORRETA
        assertThrows(TicketInvalidoException.class, () -> {
            useCase.execute(ticketId);
        });

        // Garante que o método .salvar() nunca foi chamado
        verify(repository, never()).salvar(any(Ticket.class));

    }

    @Test
    @DisplayName("Deve lançar exceção quando tentar cancelar um Ticket com uma viagem que já ocorreu.")
    void deveLancarExcecao_QuandoViagemJaOcorreu(){
        // ARRANGE
        when(viagemMock.getDataPartida()).thenReturn(LocalDateTime.now().minusDays(1));
        when(repository.buscarTicketPorId(ticketId)).thenReturn(Optional.of(ticket));

        // assert
        assertThrows(TicketInvalidoException.class, () -> {
            useCase.execute(ticketId);
        });

        // verify
        verify(repository, never()).salvar(any(Ticket.class));

    }

    @Test
    @DisplayName("Deve lançar exceção quando ticket tiver com status 'UTILIZADO'")
    void deveLancarExcecao_QuandoTicketJaEstaCancelado(){
        // ARRANGE
        Ticket ticketCancelado = new Ticket(
                ticketId, "Nome", "123", 10, new BigDecimal(100),
                FormaPagamento.PIX, StatusTicket.CANCELADO, passageiroMock, viagemMock
        );

        when(repository.buscarTicketPorId(ticketId)).thenReturn(Optional.of(ticketCancelado));

        assertThrows(TicketInvalidoException.class, () -> {
            useCase.execute(ticketId);
        });

        verify(repository, never()).salvar(any(Ticket.class));
    }
}
