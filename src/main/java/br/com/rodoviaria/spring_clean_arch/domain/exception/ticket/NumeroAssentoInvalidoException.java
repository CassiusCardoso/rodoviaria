package br.com.rodoviaria.spring_clean_arch.domain.exception.ticket;

public class NumeroAssentoInvalidoException extends RuntimeException {
    public NumeroAssentoInvalidoException(String message) {
        super(message);
    }
}
