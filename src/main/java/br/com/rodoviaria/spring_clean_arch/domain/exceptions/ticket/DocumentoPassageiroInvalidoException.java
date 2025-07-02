package br.com.rodoviaria.spring_clean_arch.Domain.Exceptions.ticket;

public class DocumentoPassageiroInvalidoException extends RuntimeException {
    public DocumentoPassageiroInvalidoException(String message) {
        super(message);
    }
}
