package br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket;

public class TicketInvalidoException extends RuntimeException {
    public TicketInvalidoException(String message) {
        super(message);
    }
}
