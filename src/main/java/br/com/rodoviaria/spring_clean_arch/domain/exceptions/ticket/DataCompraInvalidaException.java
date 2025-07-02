package br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket;

public class DataCompraInvalidaException extends RuntimeException {
    public DataCompraInvalidaException(String message) {
        super(message);
    }
}
