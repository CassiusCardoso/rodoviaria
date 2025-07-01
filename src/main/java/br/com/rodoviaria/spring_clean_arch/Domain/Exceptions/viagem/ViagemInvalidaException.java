package br.com.rodoviaria.spring_clean_arch.Domain.Exceptions.viagem;

public class ViagemInvalidaException extends RuntimeException {
    public ViagemInvalidaException(String message) {
        super(message);
    }
}
