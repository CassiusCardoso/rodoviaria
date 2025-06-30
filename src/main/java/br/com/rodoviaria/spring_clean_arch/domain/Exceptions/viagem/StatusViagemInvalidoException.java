package br.com.rodoviaria.spring_clean_arch.domain.Exceptions.viagem;

public class StatusViagemInvalidoException extends RuntimeException {
    public StatusViagemInvalidoException(String message) {
        super(message);
    }
}
