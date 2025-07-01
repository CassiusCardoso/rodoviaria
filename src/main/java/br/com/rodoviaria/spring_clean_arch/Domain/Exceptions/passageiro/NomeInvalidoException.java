package br.com.rodoviaria.spring_clean_arch.Domain.Exceptions.passageiro;

public class NomeInvalidoException extends RuntimeException {
    public NomeInvalidoException(String message) {
        super(message);
    }
}
