package br.com.rodoviaria.spring_clean_arch.domain.exception.ticket;

public class StatusInvalidoException extends RuntimeException {
    public StatusInvalidoException(String message) {
        super(message);
    }
}
