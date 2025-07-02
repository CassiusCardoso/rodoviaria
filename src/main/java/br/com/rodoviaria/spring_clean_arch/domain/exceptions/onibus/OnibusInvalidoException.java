package br.com.rodoviaria.spring_clean_arch.domain.exceptions.onibus;

public class OnibusInvalidoException extends RuntimeException {
    public OnibusInvalidoException(String message) {
        super(message);
    }
}
