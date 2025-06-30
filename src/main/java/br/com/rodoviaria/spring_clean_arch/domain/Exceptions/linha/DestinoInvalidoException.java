package br.com.rodoviaria.spring_clean_arch.domain.Exceptions.linha;

public class DestinoInvalidoException extends RuntimeException {
    public DestinoInvalidoException(String message) {
        super(message);
    }
}
