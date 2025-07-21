package br.com.rodoviaria.spring_clean_arch.infrastructure.amqp;

// IMPORTS ADICIONADOS E CORRIGIDOS
import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketEmailResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.gateway.ConsultarViagemGateway;
import br.com.rodoviaria.spring_clean_arch.application.gateway.PdfGeneratorGateway;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.email.EmailServicePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TicketNotificationListener {
    private static final Logger log = LoggerFactory.getLogger(TicketNotificationListener.class);

    private final EmailServicePort emailService;
    private final PdfGeneratorGateway pdfGeneratorGateway;
    private final ConsultarViagemGateway consultarViagemGateway;

    public TicketNotificationListener(EmailServicePort emailService,
                                      PdfGeneratorGateway pdfGeneratorGateway,
                                      ConsultarViagemGateway consultarViagemGateway) {
        this.emailService = emailService;
        this.pdfGeneratorGateway = pdfGeneratorGateway;
        this.consultarViagemGateway = consultarViagemGateway;
    }

    @Transactional
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void onTicketCreated(TicketEmailResponse ticket) {
        // ✅ 2. A mensagem já chega como um objeto, não precisa mais do log de payload em string
        log.info("MENSAGEM RECEBIDA E DESSERIALIZADA! Ticket ID: {}", ticket.id());
        try {
            // ✅ 3. Remova a conversão manual com objectMapper. A variável 'ticket' já é o objeto pronto.

            ViagemResponse viagem = consultarViagemGateway.consultar(ticket.viagemId());
            log.info("Passo 1: Detalhes da viagem consultados com sucesso.");

            log.info("Passo 2: Gerando PDF...");
            byte[] pdfNotaFiscal = pdfGeneratorGateway.gerarPdfDeTicket(ticket, viagem);
            log.info("Passo 3: PDF gerado com sucesso! Tamanho: {} bytes.", pdfNotaFiscal.length);

            String subject = "Seu Ticket de Viagem foi Confirmado!";
            String body = String.format("Olá, %s! Agradecemos por comprar conosco...", ticket.nomePassageiroTicket());

            log.info("Passo 4: Enviando e-mail para {}...", ticket.email());
            emailService.enviarComAnexo(subject, body, ticket.email(), pdfNotaFiscal, "NotaFiscal-" + ticket.id() + ".pdf");
            log.info("Passo 5: E-MAIL ENVIADO COM SUCESSO!");

        } catch (Exception e) {
            log.error("!!! ERRO CRÍTICO AO PROCESSAR MENSAGEM DO RABBITMQ para o Ticket ID {}!!!", ticket.id(), e);
            // Considerar estratégias de retentativa ou Dead Letter Queue aqui
            throw new RuntimeException("Erro ao processar notificação de ticket criado: " + ticket.id(), e);
        }
    }
}