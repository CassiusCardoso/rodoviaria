package br.com.rodoviaria.spring_clean_arch.infrastructure.adapters.email;

import br.com.rodoviaria.spring_clean_arch.application.ports.out.email.EmailServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service // SPRING RECONHECER ESSA CLASSE COMO UM BEAN
@Profile("test")
public class MockEmailService implements EmailServicePort {
    private static final Logger log = LoggerFactory.getLogger(MockEmailService.class);

    @Override
    public void enviarEmail(String destinatario, String assunto, String corpo) {
        // No futuro, aqui entraria a lógica real de envio de e-mail
        log.info("=================================================");
        log.info("SIMULANDO ENVIO DE E-MAIL REAL:");
        log.info("Destinatário: {}", destinatario);
        log.info("Assunto: {}", assunto);
        log.info("Corpo: {}", corpo);
        log.info("=================================================");
    }

    @Override
    public void enviarComAnexo(String to, String subject, String body, byte[] attachment, String attachmentName) {
        log.info("Simulando envio de e-mail COM ANEXO:");
        log.info("Para: " + to);
        log.info("Assunto: " + subject);
        log.info("Corpo: " + body);
        log.info("Anexo: " + attachmentName + " (" + attachment.length + " bytes)");
        log.info("E-mail com anexo enviado (simulação).");
    }


}
