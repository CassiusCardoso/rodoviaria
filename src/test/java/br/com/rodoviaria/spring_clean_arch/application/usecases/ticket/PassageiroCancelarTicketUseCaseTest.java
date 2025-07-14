package br.com.rodoviaria.spring_clean_arch.application.usecases.ticket;

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
public class PassageiroCancelarTicketUseCaseTest {
    @Mock
    private TicketRepository ticketRepository;
    private Ticket ticketMockAtivo;
    @Mock
    private Passageiro passageiroMock;

    @Mock
    private Viagem viagemMock;
    @Mock
    private Onibus onibusMock;

    private UUID passageiroId;
    private UUID ticketId;

    @InjectMocks
    private PassageiroCancelarTicketUseCase passageiroCancelarTicketUseCase;

    @BeforeEach
    void setUp(){

        passageiroId = UUID.randomUUID();
        ticketId = UUID.randomUUID();

        // PASSO 2: "Ensine" o viagemMock a retornar o onibusMock
        when(viagemMock.getOnibus()).thenReturn(onibusMock);

        // BÔNUS: Já que o código vai chamar getCapacidade(), configure-o também
        // para evitar futuros erros e tornar o teste mais robusto.
        when(onibusMock.getCapacidade()).thenReturn(40); // Um valor qualquer

        // << ADICIONE ESTA LINHA
        // Configura uma data de partida padrão para o mock da viagem (ex: amanhã)
        when(viagemMock.getDataPartida()).thenReturn(LocalDateTime.now().plusDays(1));



        ticketMockAtivo = new Ticket(
                ticketId,
                "John Doe",
                "12345",
                10,
                new BigDecimal(100),
                FormaPagamento.CREDITO,
                StatusTicket.CONFIRMADO,
                passageiroMock,
                viagemMock
        );
    }

    @Test
    @DisplayName("Deve cancelar ticket com sucesso, quando informações forem válidas.")
    void deveCancelarTicket_QuandoInformacoesSaoValidas(){
        // ARRANGE
        // Configura a verificação de autorização para passar
        when(passageiroMock.getId()).thenReturn(passageiroId);

        // Configura a verificação da data da viagem para passar (viagem no futuro)
        when(viagemMock.getDataPartida()).thenReturn(LocalDateTime.now().plusDays(1));

        //  Simula o ticket sendo encontrado no banco de dados
        when(ticketRepository.buscarTicketPorId(ticketId)).thenReturn(Optional.of(ticketMockAtivo));

        // (Opcional, mas recomendado) Simula o comportamento do método 'cancelar' do ticket
        // Se o seu método `cancelar` cria um novo objeto, esta é uma boa forma de testar.
        // Vamos usar um ArgumentCaptor para uma abordagem mais robusta.

        // ACT
        passageiroCancelarTicketUseCase.execute(ticketId, passageiroId);

        // ASSERT & VERIFY
        // << MELHORIA >> Usar ArgumentCaptor para capturar o objeto que foi salvo
        // Isso permite fazer assertivas fortes sobre o estado final do objeto.
        ArgumentCaptor<Ticket> ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);

        // Verifica se o método salvar foi chamado 1 vez e captura o argumento passado para ele
        verify(ticketRepository, times(1)).salvar(ticketArgumentCaptor.capture());

        // Pega o ticket que foi capturado
        Ticket ticketSalvo = ticketArgumentCaptor.getValue();

        // Agora, verificamos se o ticket salvo está com o estado correto
        assertEquals(StatusTicket.CANCELADO, ticketSalvo.getStatus(), "O status do ticket deveria ser CANCELADO.");
        assertEquals(ticketId, ticketSalvo.getId(), "O ID do ticket salvo deve ser o mesmo do original.");
    }

    @Test
    @DisplayName("Deve lançar exceção quando viagem já ocorreu.")
    void deveLancarExcecao_QuandoViagemJaOcorreu(){
        // ARRANGE

        // Configura a verificação de autorização para passar
        when(passageiroMock.getId()).thenReturn(passageiroId);

        when(ticketRepository.buscarTicketPorId(ticketId)).thenReturn(Optional.of(ticketMockAtivo));

        // Configura a verificação da data da viagem para passar (viagem já ocorreu)
        when(viagemMock.getDataPartida()).thenReturn(LocalDateTime.now().minusDays(1));

        assertThrows(TicketInvalidoException.class, () ->{
            passageiroCancelarTicketUseCase.execute(ticketId, passageiroId);
        });

        // verify
        verify(ticketRepository, times(1)).buscarTicketPorId(ticketId);

    }

    @Test
    @DisplayName("Deve lançar exceção quando ticket já estiver cancelado.")
    void deveLancarExcecao_QuandoTicketJaEstiverCancelado(){
        // ARRANGE
        // Cria um ticket que já está com o status CANCELADO
        Ticket ticketCancelado = new Ticket(
                ticketId,
                "John Doe",
                "12345",
                10,
                new BigDecimal(100),
                FormaPagamento.CREDITO,
                StatusTicket.CANCELADO,
                passageiroMock,
                viagemMock
        );

        // Ensina o repositório a retornar o ticket JÁ CANCELADO
        when(ticketRepository.buscarTicketPorId(ticketId)).thenReturn(Optional.of(ticketCancelado));

        // Configura a verificação de autorização para passar
        when(passageiroMock.getId()).thenReturn(passageiroId);

        // Configura a verificação da data da viagem para passar (viagem no futuro)
        when(viagemMock.getDataPartida()).thenReturn(LocalDateTime.now().plusDays(1));

        assertThrows(TicketInvalidoException.class, () -> {
            passageiroCancelarTicketUseCase.execute(ticketId, passageiroId);
        });
        verify(ticketRepository, times(1)).buscarTicketPorId(ticketId);
        verify(ticketRepository, never()).salvar(any(Ticket.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando status do ticket for utilizado.")
    void deveLancarExcecao_QuandoTicketJaFoiUtilizado(){
        // ARRANGE
        Ticket ticketUtilizado = new Ticket(
                ticketId,
                "John Doe",
                "12345",
                10,
                new BigDecimal(100),
                FormaPagamento.CREDITO,
                StatusTicket.UTILIZADO,
                passageiroMock,
                viagemMock
        );


        when(ticketRepository.buscarTicketPorId(ticketId)).thenReturn(Optional.of(ticketUtilizado));

        when(passageiroMock.getId()).thenReturn(passageiroId);

        assertThrows(TicketInvalidoException.class, () -> {
            passageiroCancelarTicketUseCase.execute(ticketId, passageiroId);
        });

        verify(ticketRepository, times(1)).buscarTicketPorId(ticketId);
        verify(ticketRepository, never()).salvar(any(Ticket.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando ticket não for encontrado.")
    void deveLancarExcecao_QuandoTicketNaoForEncontrado(){
        // ARRANGE
        when(ticketRepository.buscarTicketPorId(ticketId)).thenReturn(Optional.empty());

        assertThrows(TicketInvalidoException.class, () -> {
            passageiroCancelarTicketUseCase.execute(ticketId, passageiroId);
        });

        // verify
        verify(ticketRepository, times(1)).buscarTicketPorId(ticketId);
        verify(ticketRepository, never()).salvar(any(Ticket.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for dono do ticket.")
    void deveLancarExcecao_QuandoUsuarioNaoForDonoDoTicket(){
        // ARRANGE
        when(ticketRepository.buscarTicketPorId(ticketId)).thenReturn(Optional.of(ticketMockAtivo));
        when(passageiroMock.getId()).thenReturn(UUID.randomUUID());

        assertThrows(AutorizacaoInvalidaException.class, () -> {
            passageiroCancelarTicketUseCase.execute(ticketId, passageiroId);
        });

        verify(ticketRepository, times(1)).buscarTicketPorId(ticketId);
        verify(ticketRepository, never()).salvar(any(Ticket.class));

    }

    @Test
    @DisplayName("Deve lançar exceção quando id do ticket for nulo.")
    void deveLancarExcecao_QuandoIdDoTicketForNulo(){

        // assert
        assertThrows(TicketInvalidoException.class, () -> {
            passageiroCancelarTicketUseCase.execute(null, passageiroId);
        });

        // verify
        verify(ticketRepository, never()).buscarTicketPorId(ticketId);
        verify(ticketRepository, never()).salvar(any(Ticket.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando id do passageiro for nulo")
    void deveLancarExcecao_QuandoIdDoPassageiroForNulo(){

        // assert
        assertThrows(TicketInvalidoException.class, () -> {
            passageiroCancelarTicketUseCase.execute(ticketId, null);
        });

        // verify
        verify(ticketRepository, never()).buscarTicketPorId(ticketId);
        verify(ticketRepository, never()).salvar(any(Ticket.class));
    }
}

