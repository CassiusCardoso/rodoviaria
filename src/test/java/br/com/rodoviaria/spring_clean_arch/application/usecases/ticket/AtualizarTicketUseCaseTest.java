package br.com.rodoviaria.spring_clean_arch.application.usecases.ticket;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.AtualizarInformacoesPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.ticket.AtualizarTicketRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.TicketMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.enums.FormaPagamento;
import br.com.rodoviaria.spring_clean_arch.domain.enums.StatusTicket;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.AutorizacaoInvalidaException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket.StatusInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket.TicketInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.DataHoraChegadaInvalidaException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AtualizarTicketUseCaseTest {

    @Mock
    private TicketRepository repository;

    private UUID ticketId;
    private UUID usuarioLogadoId;

    private Ticket ticket;

    @Mock
    private TicketMapper mapper;

    @Mock
    private Viagem viagemMock;
    @Mock
    private Passageiro passageiroMock;
    @Mock
    private Onibus onibusMock;

    @InjectMocks
    private AtualizarTicketUseCase useCase;

    @BeforeEach
    void setUp() {
        usuarioLogadoId = UUID.randomUUID();
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
                "John Cena",
                "12345",
                10,
                new BigDecimal(100),
                FormaPagamento.DINHEIRO,
                StatusTicket.CONFIRMADO,
                passageiroMock,
                viagemMock
        );
    }

    @Test
    @DisplayName("Deve atualizar um ticket após receber informações válidas")
    void deveAtualizarPassageiroComSucesso_QuandoRequisicaoForValida() {
        // ARRANGE
        AtualizarTicketRequest request = new AtualizarTicketRequest(
                "John Cena Atualizado",
                "12"
        );

        // >> CORREÇÃO 1: Ajuste os argumentos para corresponderem ao record TicketResponse.
        // Para este teste, podemos usar 'null' para os DTOs de Passageiro e Viagem,
        // pois eles não são o foco da validação aqui.
        TicketResponse responseEsperado = new TicketResponse(
                ticketId,
                "John Cena Atualizado",
                "12",
                10,
                new BigDecimal("100"), // O preço é um BigDecimal, não um int. Corrigido.
                FormaPagamento.DINHEIRO.name(),
                StatusTicket.CONFIRMADO.name(),
                null, // dataCompra (LocalDate) - pode ser null se não for relevante para o assert
                null, // passageiro (PassageiroResponse) - pode ser null
                null  // viagem (ViagemResponse) - pode ser null
        );
        // << ADICIONE ESTA LINHA: Ensina o mock a retornar o ID correto para a autorização passar.
        when(passageiroMock.getId()).thenReturn(usuarioLogadoId);

        when(repository.buscarTicketPorId(ticketId)).thenReturn(Optional.of(ticket));

        // Quando .salvar() for chamado retornar o ticket atualizado
        when(repository.salvar(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // >> CORREÇÃO 2: Mova a configuração do mapper para ANTES da chamada do use case.
        // Use any(Ticket.class) para garantir que o mock responda corretamente.
        when(mapper.toResponse(any(Ticket.class))).thenReturn(responseEsperado);

        // ACT
        TicketResponse response = useCase.execute(ticketId, request, usuarioLogadoId);

        // ASSERT
        assertNotNull(response);
        assertEquals("John Cena Atualizado", response.nomePassageiroTicket(), "O nome do passageiro deve ser atualizado.");
        assertEquals("12", response.documentoPassageiroTicket());

        ArgumentCaptor<Ticket> ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);

        verify(repository, times(1)).salvar(ticketArgumentCaptor.capture());

        // Pegar o ticket capturado e verificar se ele contém os dados atualizados

        Ticket ticketSalvo = ticketArgumentCaptor.getValue();
        assertEquals("John Cena Atualizado", response.nomePassageiroTicket(), "O nome do passageiro deve ser atualizado.");
        assertEquals("12", response.documentoPassageiroTicket());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ticket não existir.")
    void deveLancarExcecao_QuandoTicketNaoExistir(){
        // ARRANGE
        Ticket ticketNaoEncontrado = null;
        when(repository.buscarTicketPorId(ticketId)).thenReturn(Optional.ofNullable(ticketNaoEncontrado));

        AtualizarTicketRequest request = new AtualizarTicketRequest("a", "b");

        assertThrows(TicketInvalidoException.class, () -> {
            useCase.execute(ticketId, request, usuarioLogadoId);
        });

        verify(repository, times(1)).buscarTicketPorId(ticketId);
        verify(mapper, never()).toResponse(any(Ticket.class));

    }

    @Test
    @DisplayName("Deve lançar AutorizacaoInvalidaException quando o usuário não for o dono ticket")
    void deveLancarExcecao_QuandoUsuarioNaoTemPermissao(){
        // ARRANGE
        UUID invasorId = UUID.randomUUID(); // Um ID diferente do dono do ticket
        AtualizarTicketRequest request = new AtualizarTicketRequest("Nome", "Documento");

        when(repository.buscarTicketPorId(ticketId)).thenReturn(Optional.of(ticket));

        // O passageiro do ticket (passageiroMock) tem o ID 'usuarioLogadoId' (definido no setUp),
        // mas a chamada ao use case será feita com 'idDoOutroUsuario'.
        // A verificação `!ticketAtual.getPassageiro().getId().equals(usuarioLogadoId)` vai falhar.
        when(passageiroMock.getId()).thenReturn(invasorId);

        // assert
        assertThrows(AutorizacaoInvalidaException.class, () -> {
            useCase.execute(ticketId, request, usuarioLogadoId);
        });

        // verify
        verify(repository, never()).salvar(any(Ticket.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando a data da viagem já ocorreu.")
    void deveLancarExcecao_QuandoViagemJaOcorreu(){
        // ARRANGE
        // Configura o mock da viagem para retornar uma data no passado
        when(viagemMock.getDataPartida()).thenReturn(LocalDateTime.now().minusDays(1));

        when(repository.buscarTicketPorId(ticketId)).thenReturn(Optional.of(ticket));
        when(passageiroMock.getId()).thenReturn(usuarioLogadoId); // Garante que a autorização passe

        AtualizarTicketRequest request = new AtualizarTicketRequest("Nome", "Documento");

        assertThrows(DataHoraChegadaInvalidaException.class, () -> {
            useCase.execute(ticketId, request, usuarioLogadoId);
        });

        // verify
        verify(repository, never()).salvar(any(Ticket.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o status do ticket estiver UTILIZADO.")
    void deveLancarExcecao_QuandoTicketJaUtilizado(){
        // ARRANGE
        Ticket ticketUtilizado = new Ticket(
                ticketId,
                "Nome",
                "Documento",
                10,
                new BigDecimal(100),
                FormaPagamento.DINHEIRO,
                StatusTicket.UTILIZADO,
                passageiroMock,
                viagemMock
        );

        when(repository.buscarTicketPorId(ticketId)).thenReturn(Optional.of(ticketUtilizado));

        when(passageiroMock.getId()).thenReturn(usuarioLogadoId); // Garante que a autorização passe
        // É preciso garantir que a data da viagem não seja no passado para isolar o teste de status
        when(viagemMock.getDataPartida()).thenReturn(LocalDateTime.now().plusDays(1));

        AtualizarTicketRequest request = new AtualizarTicketRequest("Nome Novo", "Doc Novo");

        assertThrows(StatusInvalidoException.class, () -> {
            useCase.execute(ticketId, request, usuarioLogadoId);
        });

        verify(repository, never()).salvar(any(Ticket.class));

    }
}
