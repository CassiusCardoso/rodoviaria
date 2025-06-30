package br.com.rodoviaria.spring_clean_arch.domain.Exceptions.ticket;

public class StatusInvalidoException extends RuntimeException {
    public StatusInvalidoException(String message) {
        super(message);
    }
}
