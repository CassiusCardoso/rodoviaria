package br.com.rodoviaria.spring_clean_arch.Domain.Exceptions.onibus;

public class PlacaInvalidaException extends RuntimeException {
    public PlacaInvalidaException(String message) {
        super(message);
    }
}
