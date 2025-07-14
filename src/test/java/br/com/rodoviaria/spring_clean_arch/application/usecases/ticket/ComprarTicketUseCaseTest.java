package br.com.rodoviaria.spring_clean_arch.application.usecases.ticket;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.ticket.ComprarTicketRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketResponse;
import br.com.rodoviaria.spring_clean_arch.application.mapper.TicketMapper;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Onibus;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Ticket;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Viagem;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro.PassageiroInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket.AssentoInvalidoException;
import br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket.DataCompraInvalidaException;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComprarTicketUseCaseTest {
    @Mock
    private ViagemRepository viagemRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private PassageiroRepository passageiroRepository;

    @Mock
    private TicketMapper ticketMapper;

    private ComprarTicketRequest request;

    private Viagem viagemMock;
    private Passageiro passageiroMock;
    private Onibus onibusMock;

    @InjectMocks
    private ComprarTicketUseCase comprarTicketUseCase;
    private Ticket ticket;

    @BeforeEach
    void setUp(){
        request = new ComprarTicketRequest(
                "John Doe", "12345", 10, new BigDecimal( 105.50),"PIX", UUID.randomUUID(), UUID.randomUUID()
        );

        // Criar mocks das entidades para peristir nos repositórios
        viagemMock = mock(Viagem.class);
        onibusMock = mock(Onibus.class);
        passageiroMock = mock(Passageiro.class);


    }

    @Test
    @DisplayName("Deve comprar um ticket com sucesso quando todos os dados são válidos")
    void deveRetornarTicketComprado_ComSucesso(){
        // Mova as configurações para dentro do teste que as usa.
        // Simular a busca pela entidade
        when(viagemRepository.buscarViagemPorId(request.viagemId())).thenReturn(Optional.of(viagemMock));
        when(passageiroRepository.buscarPassageiroPorId(request.compradorId())).thenReturn(Optional.of(passageiroMock));
        // Defina
        when(viagemMock.getOnibus()).thenReturn(onibusMock);
        when(onibusMock.getCapacidade()).thenReturn(40);

        // Ensine o mock da viagem a retornar uma data futura quando getDataPartida() for chamado.
        // Isso fará com que a validação no construtor do Ticket passe.
        when(viagemMock.getDataPartida()).thenReturn(LocalDateTime.now().plusDays(1));

        // Verificar se um assento específico está ocupado
        when(ticketRepository.assentoOcupado(viagemMock, request.numeroAssento())).thenReturn(false);

        // Simular que está salvando o ticket
        when(ticketRepository.salvar(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Simular o mapeamento
        when(ticketMapper.toResponse(any(Ticket.class))).thenReturn(mock(TicketResponse.class));

        // ACT e ASSERT

        TicketResponse response = comprarTicketUseCase.execute(request);

        // ASSERT
        assertNotNull(response, "Não deve ser nula");


        // --- VERIFY ---
        // CORREÇÃO: Verifique a chamada com o ID correto do request.
        verify(ticketRepository, times(1)).salvar(any(Ticket.class));

    }

    @Test
    @DisplayName("Deve lançar exceção e não comprar ticket quando assento já estiver ocupado.")
    void deveLancarExcecao_QuandoAssentoJaEstiverOcupado(){
        // ARRANGE
        // Simule que a viagem e o passageiro foram encontrados com sucesso.
        when(viagemRepository.buscarViagemPorId(request.viagemId())).thenReturn(Optional.of(viagemMock));
        when(passageiroRepository.buscarPassageiroPorId(request.compradorId())).thenReturn(Optional.of(passageiroMock));

        // Simule a regra de negócio de falha: o assento está OCUPADO.
        when(ticketRepository.assentoOcupado(viagemMock, request.numeroAssento())).thenReturn(true);

        // NÃO PRECISA USAR viagemMock para pegar a data de partida

        // ACT e assert
        assertThrows(AssentoInvalidoException.class, () -> {
            comprarTicketUseCase.execute(request);
                });

        // --- VERIFY ---
        // Garanta que, como a validação de assento falhou, o método 'salvar' NUNCA foi chamado.
        verify(ticketRepository, never()).salvar(any(Ticket.class));
    }

    @Test
    @DisplayName("Deve lançar exceção e não comprar ticket quando não existir um comprador.")
    void deveLancarExcecao_QuandoPassageiroCompradorNaoExiste(){
        // ARRANGE
        when(viagemRepository.buscarViagemPorId(request.viagemId())).thenReturn(Optional.of(viagemMock));
        when(passageiroRepository.buscarPassageiroPorId(request.compradorId())).thenReturn(Optional.empty());

        // NÃO PRECISA VERIFICAR SE O ASSENTO ESTÁ OCUPADO

        // ACT e ASSERT
        assertThrows(PassageiroInvalidoException.class, () -> {
            comprarTicketUseCase.execute(request);
        });

        verify(ticketRepository, never()).salvar(any(Ticket.class));
    }

    @Test
    @DisplayName("Deve lançar exceção e não comprar ticket quando não existir uma viagem.")
    void deveLancarExcecao_QuandoViagemNaoExiste(){
        // ARRANGE
        when(viagemRepository.buscarViagemPorId(request.viagemId())).thenReturn(Optional.empty());

        // ACT e ASSERT
        assertThrows(ViagemInvalidaException.class, () -> {
            comprarTicketUseCase.execute(request);
        });

        verify(ticketRepository, never()).salvar(any(Ticket.class));
    }
    @Test
    @DisplayName("Deve lançar exceção ao tentar comprar ticket para uma viagem que já partiu")
    void deveLancarExcecao_QuandoViagemJaPartiu(){
        // ARRANGE
        // Sobrescreva o mock para simular uma viagem que já ocorreu
        when(viagemMock.getDataPartida()).thenReturn(LocalDateTime.now().minusDays(1));

        // 1. Configure o comportamento dos mocks aninhados para evitar o NPE.
        when(viagemMock.getOnibus()).thenReturn(onibusMock);
        when(onibusMock.getCapacidade()).thenReturn(40); // A capacidade pode ser qualquer valor.


        when(viagemRepository.buscarViagemPorId(request.viagemId())).thenReturn(Optional.of(viagemMock));
        when(passageiroRepository.buscarPassageiroPorId(request.compradorId())).thenReturn(Optional.of(passageiroMock));
        when(ticketRepository.assentoOcupado(viagemMock, request.numeroAssento())).thenReturn(false);

        // ACT e ASSERT
        assertThrows(DataCompraInvalidaException.class, () -> {
            comprarTicketUseCase.execute(request);
        });

        // VERIFY
        verify(ticketRepository, never()).salvar(any(Ticket.class));
    }

}
