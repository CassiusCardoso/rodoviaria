package br.com.rodoviaria.spring_clean_arch.Domain.Exceptions.passageiro;

public class AutorizacaoInvalidaException extends RuntimeException {
    public AutorizacaoInvalidaException(String message) {
        super(message);
    }
}
