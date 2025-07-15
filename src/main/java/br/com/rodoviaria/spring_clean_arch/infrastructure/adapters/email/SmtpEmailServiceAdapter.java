package br.com.rodoviaria.spring_clean_arch.infrastructure.adapters.email;

import br.com.rodoviaria.spring_clean_arch.application.ports.out.email.EmailServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Primary // Diz ao spring para preferir esta implementação em vez do MockEmailService
public class SmtpEmailServiceAdapter implements EmailServicePort {

    private static final Logger log = LoggerFactory.getLogger(SmtpEmailServiceAdapter.class);


    private final JavaMailSender javaMailSender;

    @Autowired
    public SmtpEmailServiceAdapter(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void enviarEmail(String destinatario, String assunto, String corpo){
        log.info("Iniciando envio de e-mail real para: {}", destinatario);
        try{
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setTo(destinatario);
            mensagem.setSubject(assunto);
            mensagem.setText(corpo);
            javaMailSender.send(mensagem);
            log.info("E-mail enviado com sucesso para: {}", destinatario);
        } catch (Exception e) {
            log.error("Erro ao enviar e-mail para: {}", destinatario, e);
        }
    }
}
