package br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem;

public class LinhaInvalidaException extends RuntimeException {
    public LinhaInvalidaException(String message) {
        super(message);
    }
}
