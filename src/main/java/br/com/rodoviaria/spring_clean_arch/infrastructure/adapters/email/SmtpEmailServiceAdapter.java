package br.com.rodoviaria.spring_clean_arch.infrastructure.adapters.email;

import br.com.rodoviaria.spring_clean_arch.application.ports.out.email.EmailServicePort;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
    public void enviarEmail(String destinatario, String assunto, String corpo) {
        log.info("Iniciando envio de e-mail real para: {}", destinatario);
        try {
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

    // ✅ IMPLEMENTE O NOVO MÉTODO AQUI
    @Override
    public void enviarComAnexo(String titulo, String mensagem, String destinatario, byte[] anexo, String nomeAnexo) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            // O 'true' no construtor habilita o modo multipart, essencial para anexos
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("nao-responda@rodoviaria.com"); // Ou seu e-mail de remetente
            helper.setTo(destinatario);
            helper.setSubject(titulo);
            helper.setText(mensagem, true); // O 'true' permite que a mensagem seja em HTML

            // A mágica acontece aqui: adicionando o anexo
            helper.addAttachment(nomeAnexo, new ByteArrayResource(anexo));

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            // É uma boa prática logar o erro
            // log.error("Falha ao enviar e-mail com anexo para {}", destinatario, e);
            throw new RuntimeException("Falha ao enviar e-mail com anexo", e);
        }
    }
}
