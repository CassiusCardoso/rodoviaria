package br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem;

public class ViagemInvalidaException extends RuntimeException {
    public ViagemInvalidaException(String message) {
        super(message);
    }
}
