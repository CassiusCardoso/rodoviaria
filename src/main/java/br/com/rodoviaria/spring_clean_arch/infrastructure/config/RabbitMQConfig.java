package br.com.rodoviaria.spring_clean_arch.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory; // IMPORTAR

@Configuration
public class RabbitMQConfig {

    public static final String ROUTING_KEY_TICKET_EMAIL = "ticket.email.notification";
    // É uma boa prática pegar o nome da fila do application.properties
    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    // 1. Declara a fila. O nome agora vem do seu arquivo de configuração.
    @Bean
    public Queue queue() {
        // O 'true' significa que a fila é "durável" (sobrevive a reinicializações do RabbitMQ)
        return new Queue(queueName, true);
    }

    // 2. Declara uma "exchange" (o carteiro que decide para qual fila enviar)
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    // 3. Declara o "binding" (a regra que liga a exchange à fila usando o nome da fila como chave de roteamento)
    @Bean
    public Binding binding(Queue ticketEmailQueue, TopicExchange ticketExchange) {
        return BindingBuilder.bind(ticketEmailQueue).to(ticketExchange).with(ROUTING_KEY_TICKET_EMAIL);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}

