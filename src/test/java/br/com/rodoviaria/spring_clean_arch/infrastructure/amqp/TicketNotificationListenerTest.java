package br.com.rodoviaria.spring_clean_arch.infrastructure.amqp;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketEmailResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.gateway.ConsultarViagemGateway;
import br.com.rodoviaria.spring_clean_arch.application.gateway.PdfGeneratorGateway;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.email.EmailServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketNotificationListenerTest {

    @Mock
    private EmailServicePort emailService;
    @Mock
    private PdfGeneratorGateway pdfGeneratorGateway;
    @Mock
    private ConsultarViagemGateway consultarViagemGateway;

    private TicketNotificationListener listener; // Nome simplificado

    // Variáveis que serão usadas nos testes
    private TicketEmailResponse ticketDto;
    private ViagemResponse viagemResponse;

    @BeforeEach
    void setUp() {
        // --- PREPARAÇÃO DOS DADOS DE TESTE ---
        // Não precisamos mais de ObjectMapper ou jsonPayload

        UUID viagemId = UUID.randomUUID();

        LinhaResponse linhaDummy = new LinhaResponse(UUID.randomUUID(), "Origem Teste", "Destino Teste", 100, true, LocalDateTime.now());
        OnibusResponse onibusDummy = new OnibusResponse(UUID.randomUUID(), "Modelo Teste", "Placa Teste", 40, true, LocalDateTime.now());

        viagemResponse = new ViagemResponse(
                viagemId,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "ATIVA",
                "Origem teste",
                "Destino teste"
        );

        ticketDto = new TicketEmailResponse(
                UUID.randomUUID(),
                viagemId,
                "Passageiro de Teste",
                "teste@unitario.com",
                "12345678900",
                new BigDecimal("150.75"),
                LocalDate.now()
        );

        // ✅ 1. Instanciamos o listener com o construtor correto (3 argumentos)
        listener = new TicketNotificationListener(
                emailService,
                pdfGeneratorGateway,
                consultarViagemGateway
        );
    }

    @Test
    void deveProcessarNotificacaoComSucesso() {
        // --- ARRANGE (Configuração dos Mocks) ---
        byte[] fakePdf = "PDF Falso".getBytes();
        when(consultarViagemGateway.consultar(ticketDto.viagemId())).thenReturn(viagemResponse);
        when(pdfGeneratorGateway.gerarPdfDeTicket(ticketDto, viagemResponse)).thenReturn(fakePdf);
        // O mock do emailService não precisa de 'when' porque o método retorna void.

        // --- ACT (Execução do método a ser testado) ---
        // ✅ 2. Chamamos o método passando o objeto DTO diretamente, não mais a string JSON.
        listener.onTicketCreated(ticketDto);

        // --- ASSERT (Verificação dos resultados) ---
        // Verificamos se o método de consulta de viagem foi chamado uma vez com o ID correto.
        verify(consultarViagemGateway, times(1)).consultar(ticketDto.viagemId());

        // Verificamos se o gerador de PDF foi chamado uma vez com os dados corretos.
        verify(pdfGeneratorGateway, times(1)).gerarPdfDeTicket(ticketDto, viagemResponse);

        // Verificamos se o serviço de e-mail foi chamado uma vez para o destinatário correto.
        verify(emailService, times(1)).enviarComAnexo(
                anyString(),
                anyString(),
                eq("teste@unitario.com"),
                eq(fakePdf),
                anyString()
        );
    }
}