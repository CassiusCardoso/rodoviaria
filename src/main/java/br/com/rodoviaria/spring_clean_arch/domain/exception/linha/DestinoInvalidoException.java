package br.com.rodoviaria.spring_clean_arch.domain.exception.linha;

public class DestinoInvalidoException extends RuntimeException {
    public DestinoInvalidoException(String message) {
        super(message);
    }
}
