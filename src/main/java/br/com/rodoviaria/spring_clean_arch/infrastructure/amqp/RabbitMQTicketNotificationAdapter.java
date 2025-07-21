package br.com.rodoviaria.spring_clean_arch.infrastructure.amqp;

import br.com.rodoviaria.spring_clean_arch.application.dto.response.ticket.TicketEmailResponse;
import br.com.rodoviaria.spring_clean_arch.application.gateway.TicketNotificationGateway;
import br.com.rodoviaria.spring_clean_arch.infrastructure.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// ✅ Anotação que resolve o erro! Informa ao Spring que esta é uma classe gerenciável (um bean).
@Component
public class RabbitMQTicketNotificationAdapter implements TicketNotificationGateway {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQTicketNotificationAdapter.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;

    // O Spring injetará o RabbitTemplate e o valor da propriedade aqui.
    public RabbitMQTicketNotificationAdapter(RabbitTemplate rabbitTemplate,
                                             @Value("${rabbitmq.exchange.name}") String exchangeName) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
    }

    // Implementação do método definido na interface da camada de aplicação.
    @Override
    public void enviarNotificacao(TicketEmailResponse ticketData) {
        log.info("Publicando mensagem via RabbitMQ para o ticket ID: {}", ticketData.id());

        rabbitTemplate.convertAndSend(
                exchangeName,
                RabbitMQConfig.ROUTING_KEY_TICKET_EMAIL,
                ticketData
        );
    }
}