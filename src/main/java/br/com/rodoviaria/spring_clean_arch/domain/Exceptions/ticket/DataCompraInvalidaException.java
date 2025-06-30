package br.com.rodoviaria.spring_clean_arch.domain.Exceptions.ticket;

public class DataCompraInvalidaException extends RuntimeException {
    public DataCompraInvalidaException(String message) {
        super(message);
    }
}
