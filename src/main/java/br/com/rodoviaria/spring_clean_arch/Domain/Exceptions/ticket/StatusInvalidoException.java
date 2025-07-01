package br.com.rodoviaria.spring_clean_arch.Domain.Exceptions.ticket;

public class StatusInvalidoException extends RuntimeException {
    public StatusInvalidoException(String message) {
        super(message);
    }
}
