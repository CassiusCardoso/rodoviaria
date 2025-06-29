package br.com.rodoviaria.spring_clean_arch.domain.exception.viagem;

public class LinhaInvalidaException extends RuntimeException {
    public LinhaInvalidaException(String message) {
        super(message);
    }
}
