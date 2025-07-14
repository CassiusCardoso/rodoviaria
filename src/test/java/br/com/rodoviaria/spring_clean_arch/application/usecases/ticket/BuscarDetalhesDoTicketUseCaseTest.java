package br.com.rodoviaria.spring_clean_arch.application.usecases.ticket;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.TicketMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.enums.FormaPagamento;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusTicket;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket.TicketInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuscarDetalhesDoTicketUseCaseTest {
    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketMapper ticketMapper;

    private UUID ticketId;
    private UUID usuarioLogadoId;

    private Ticket ticketMock;
    private Passageiro passageiroMock;
    private Onibus onibusMock;
    private Viagem viagemMock;

    @InjectMocks
    private BuscarDetalhesDoTicketUseCase buscarDetalhesDoTicketUseCase;

    @BeforeEach
    void setUp() {
        // 1. Defina os IDs que serão usados nos testes.
        ticketId = UUID.randomUUID();
        usuarioLogadoId = UUID.randomUUID();

        // 2. Crie mocks das entidades dependentes.
        passageiroMock = mock(Passageiro.class);
        viagemMock = mock(Viagem.class);
        onibusMock = mock(Onibus.class); // Crie o mock do ônibus

        // Ensine os mocks a se comportarem em cadeia:
        // 1. Quando chamarem getOnibus() na viagem, retorne o mock do ônibus.
        when(viagemMock.getOnibus()).thenReturn(onibusMock);
        // 2. Quando chamarem getCapacidade() no ônibus, retorne um valor qualquer (ex: 40).
        when(onibusMock.getCapacidade()).thenReturn(40);
        // 3. A data de partida também é necessária para o construtor do Ticket.
        when(viagemMock.getDataPartida()).thenReturn(LocalDateTime.now().plusDays(1));

        // 4. Crie uma instância REAL do Ticket usando os mocks.
        //    Este é o objeto que o repositório vai "encontrar".
        ticketMock = new Ticket(
                ticketId,
                "Passageiro Dono",
                "123456",
                10,
                new BigDecimal("100"),
                FormaPagamento.PIX,
                StatusTicket.CONFIRMADO,
                passageiroMock, // Passe o mock do passageiro
                viagemMock    // Passe o mock da viagem
        );
    }

    @Test
    @DisplayName("Deve retornar ticket com sucesso, quando o ticket existe.")
    void deveRetornarDetalhesDoTicket_QuandoTicketExisteEUsuarioEDono(){
        // BUSCANDO O TICKET
        when(ticketRepository.buscarTicketPorId(ticketId)).thenReturn(Optional.of(ticketMock));

        // 4. "Ensine" o mock do passageiro a retornar o ID correto.
        when(passageiroMock.getId()).thenReturn(usuarioLogadoId);

        // 2. Crie um DTO de resposta falso para o mapper retornar. (Sua terceira lógica)
        //    Isso isola o teste da implementação real do mapper.
        TicketResponse responseFalsa = new TicketResponse(
                ticketId, "Passageiro Dono", "123456", 10, new BigDecimal("100"),
                "PIX", "CONFIRMADO", null, null, null // Outros campos não importam para este teste
        );

        when(ticketMapper.toResponse(ticketMock)).thenReturn(responseFalsa);

        // ACT - AGIR
        // Execute o caso de uso. A verificação do ID acontece aqui
        TicketResponse response = buscarDetalhesDoTicketUseCase.execute(ticketId, usuarioLogadoId);

        // ASSERT - verificar

        // Verifique a resposta retornada.
        assertNotNull(response, "A resposta não deveria ser nula.");
        assertEquals(ticketId, response.id(), "O ID do ticket na resposta deve ser o mesmo do request.");
        assertEquals("Passageiro Dono", response.nomePassageiroTicket(), "O nome do passageiro na resposta deve ser o mesmo do request.");

        // VERIFY
        verify(ticketRepository, times(1)).buscarTicketPorId(ticketId);
        // Garanta que conforme a autorização passe, o mapper seja chamado para criar a resposta.
        verify(ticketMapper, times(1)).toResponse(ticketMock);
    }

    @Test
    @DisplayName("Deve lançar exceção quando autorização não coincidir.")
    void deveLancarExcecaoDeAutorizacao_QuandoUsuarioNaoEDonoDoTicket(){
        // ARRANGE
        when(ticketRepository.buscarTicketPorId(ticketId)).thenReturn(Optional.of(ticketMock));
        // 4. "Ensine" o mock do passageiro a retornar o ID correto.
        when(passageiroMock.getId()).thenReturn(usuarioLogadoId);
        // CRIAR UM UUID para um invasor
        UUID invasorId = UUID.randomUUID();

        // ACT e ASSERT
        assertThrows(AutorizacaoInvalidaException.class, () -> {
            buscarDetalhesDoTicketUseCase.execute(ticketId, invasorId);
        });

        // VERIFY
        verify(ticketRepository, times(1)).buscarTicketPorId(ticketId);

        // Garantir que, por causa da falha de autorização, o mapper
        // NUNCA foi chamado para criar uma resposta.
        verify(ticketMapper, never()).toResponse(any(Ticket.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando ticket não existe.")
    void deveLancarExcecao_QuandoTicketNaoExiste(){
        when(ticketRepository.buscarTicketPorId(ticketId)).thenReturn(Optional.empty());

        assertThrows(TicketInvalidoException.class, () -> {
            buscarDetalhesDoTicketUseCase.execute(ticketId, usuarioLogadoId);
        });

        verify(ticketRepository, times(1)).buscarTicketPorId(ticketId);
        verify(ticketMapper, never()).toResponse(any(Ticket.class));
    }
}
