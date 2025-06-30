package br.com.rodoviaria.spring_clean_arch.domain.Exceptions.ticket;

public class NumeroAssentoInvalidoException extends RuntimeException {
    public NumeroAssentoInvalidoException(String message) {
        super(message);
    }
}
