package br.com.rodoviaria.spring_clean_arch.domain.Exceptions.onibus;

public class PlacaInvalidaException extends RuntimeException {
    public PlacaInvalidaException(String message) {
        super(message);
    }
}
