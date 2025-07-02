package br.com.rodoviaria.spring_clean_arch.domain.exceptions.linha;

public class OrigemInvalidoException extends RuntimeException {
    public OrigemInvalidoException(String message) {
        super(message);
    }
}
