package br.com.rodoviaria.spring_clean_arch.domain.exception.ticket;

public class DataCompraInvalidaException extends RuntimeException {
    public DataCompraInvalidaException(String message) {
        super(message);
    }
}
