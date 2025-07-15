package br.com.rodoviaria.spring_clean_arch.infrastructure.amqp;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketEmailResponse;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.email.EmailServicePort;
import br.com.rodoviaria.spring_clean_arch.infrastructure.config.BeanConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TicketNotificationListener {
    private static final Logger log = LoggerFactory.getLogger(TicketNotificationListener.class);

    private final EmailServicePort emailService;
    public TicketNotificationListener(EmailServicePort emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = BeanConfiguration.QUEUE_TICKET_EMAIL)
    public void processTicketEmailNotification(TicketEmailResponse emailData){
        log.info("Mensagem recebida da fila: {}", emailData);

        // CHAMAR O SERVIÇO DE EMAIL REAL
        String to = emailData.email();
        String subject = "NF - Confirmação de compra (Ticket #)" + emailData.id();
        String body = "Olá, " + emailData.nomePassageiroTicket() + "! ...";
        emailService.enviarEmail(to, subject, body);

        System.out.println("=================================================");
        System.out.println("SIMULANDO ENVIO DE E-MAIL DE CONFIRMAÇÃO:");
        System.out.println("Para: " + emailData.email());
        System.out.println("Assunto: Confirmação de Compra - Ticket " + emailData.id());
        System.out.println("=================================================");
    }
}

