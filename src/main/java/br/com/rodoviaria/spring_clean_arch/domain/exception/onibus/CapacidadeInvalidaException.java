package br.com.rodoviaria.spring_clean_arch.domain.exception.onibus;

public class CapacidadeInvalidaException extends RuntimeException {
    public CapacidadeInvalidaException(String message) {
        super(message);
    }
}
