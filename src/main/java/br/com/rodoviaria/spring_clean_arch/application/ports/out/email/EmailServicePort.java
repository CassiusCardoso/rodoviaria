package br.com.rodoviaria.spring_clean_arch.application.ports.out.email;

// Este é o "contrato" que o Caso de Uso irá conhecer.
public interface EmailServicePort {
    void enviarEmail(String destinatario, String assunto, String corpo);

    void enviarComAnexo(String titulo, String mensagem, String destinatario, byte[] anexo, String nomeAnexo);

}
