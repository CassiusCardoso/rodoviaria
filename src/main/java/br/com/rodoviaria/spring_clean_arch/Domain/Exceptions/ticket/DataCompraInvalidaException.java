package br.com.rodoviaria.spring_clean_arch.Domain.Exceptions.ticket;

public class DataCompraInvalidaException extends RuntimeException {
    public DataCompraInvalidaException(String message) {
        super(message);
    }
}
