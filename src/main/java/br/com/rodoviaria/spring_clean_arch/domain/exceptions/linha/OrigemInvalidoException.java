package br.com.rodoviaria.spring_clean_arch.Domain.Exceptions.linha;

public class OrigemInvalidoException extends RuntimeException {
    public OrigemInvalidoException(String message) {
        super(message);
    }
}
