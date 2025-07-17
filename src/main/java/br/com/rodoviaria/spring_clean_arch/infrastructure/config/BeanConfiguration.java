package br.com.rodoviaria.spring_clean_arch.infrastructure.config;

import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.SenhaEncoderPort;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.AdministradorRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.adapters.BCryptSenhaEncoderAdapter;
import br.com.rodoviaria.spring_clean_arch.infrastructure.security.AdminAutenticacaoService;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Properties;

@Configuration
public class BeanConfiguration {

    // Beans de configuração de serviços externos (RabbitMQ, E-mail)
    // ESTÁ PERFEITO AQUI, POIS REQUEREM CONFIGURAÇÃO EXPLÍCITA.
    // =================================================================

    public static final String EXCHANGE_NAME = "rodoviaria-exchange";
    public static final String QUEUE_TICKET_EMAIL = "ticket_email_notification_queue";
    public static final String ROUTING_KEY_TICKET_EMAIL = "ticket.email.notification";

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("cassiuscardosoo@gmail.com");
        mailSender.setPassword("nshx tdjq ctbd hekp"); // Lembre-se de mover isso para variáveis de ambiente em produção

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }

    @Bean
    public Queue queue(){
        return new Queue(QUEUE_TICKET_EMAIL, true); // true = fila durável
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_TICKET_EMAIL);
    }


    // BEANS DE INFRAESTRUTURA DE SEGURANÇA
    // ESTÁ PERFEITO AQUI, POIS SÃO ADAPTADORES E SERVIÇOS CENTRAIS.
    // =================================================================

    @Bean
    public AdminAutenticacaoService adminAutenticaoService(AdministradorRepository administradorRepository) {
        return new AdminAutenticacaoService(administradorRepository);
    }

    @Bean
    public SenhaEncoderPort senhaEncoderPort(PasswordEncoder encoder) {
        return new BCryptSenhaEncoderAdapter(encoder);
    }

}