package br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket;

public class NomePassageiroTicketInvalidoException extends RuntimeException {
    public NomePassageiroTicketInvalidoException(String message) {
        super(message);
    }
}
