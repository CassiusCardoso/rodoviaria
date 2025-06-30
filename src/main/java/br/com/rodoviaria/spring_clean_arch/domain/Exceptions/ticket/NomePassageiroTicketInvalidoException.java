package br.com.rodoviaria.spring_clean_arch.domain.Exceptions.ticket;

public class NomePassageiroTicketInvalidoException extends RuntimeException {
    public NomePassageiroTicketInvalidoException(String message) {
        super(message);
    }
}
