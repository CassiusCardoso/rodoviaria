package br.com.rodoviaria.spring_clean_arch.Domain.Exceptions.ticket;

public class PrecoInvalidoException extends RuntimeException {
    public PrecoInvalidoException(String message) {
        super(message);
    }
}
