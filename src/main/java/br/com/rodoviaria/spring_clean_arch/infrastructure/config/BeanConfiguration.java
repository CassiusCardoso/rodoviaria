package br.com.rodoviaria.spring_clean_arch.infrastructure.config;

import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.SenhaEncoderPort;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.AdministradorRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.adapters.BCryptSenhaEncoderAdapter;
import br.com.rodoviaria.spring_clean_arch.infrastructure.security.AdminAutenticacaoService;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Properties;

@Configuration
public class BeanConfiguration {

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


    /**
     * Define o conversor de mensagens padrão para JSON (usando Jackson).
     * Isso é mais seguro e interoperável que a serialização Java padrão
     * e resolve o erro de 'deserialize unauthorized class'.
     */

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