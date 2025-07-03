package br.com.rodoviaria.spring_clean_arch.application.ports.out.email;

// Este é o "contrato" que o Caso de Uso irá conhecer.
public interface EmailServicePort {
    void enviarEmail(String destinatario, String assunto, String corpo);
}
