package br.com.rodoviaria.spring_clean_arch.domain.Exceptions.ticket;

public class DocumentoPassageiroInvalidoException extends RuntimeException {
    public DocumentoPassageiroInvalidoException(String message) {
        super(message);
    }
}
