package br.com.rodoviaria.spring_clean_arch.Domain.Exceptions.ticket;

public class TicketInvalidoException extends RuntimeException {
    public TicketInvalidoException(String message) {
        super(message);
    }
}
