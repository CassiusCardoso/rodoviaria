package br.com.rodoviaria.spring_clean_arch.domain.exceptions.passageiro;

public class PassageiroInvalidoException extends RuntimeException {
    public PassageiroInvalidoException(String message) {
        super(message);
    }
}
