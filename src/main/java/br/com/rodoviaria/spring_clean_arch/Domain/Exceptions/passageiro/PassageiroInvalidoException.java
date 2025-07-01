package br.com.rodoviaria.spring_clean_arch.Domain.Exceptions.passageiro;

public class PassageiroInvalidoException extends RuntimeException {
    public PassageiroInvalidoException(String message) {
        super(message);
    }
}
