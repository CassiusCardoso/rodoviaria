package br.com.rodoviaria.spring_clean_arch.domain.exceptions.ticket;

public class AssentoInvalidoException extends RuntimeException {
    public AssentoInvalidoException(String message) {
        super(message);
    }
}
