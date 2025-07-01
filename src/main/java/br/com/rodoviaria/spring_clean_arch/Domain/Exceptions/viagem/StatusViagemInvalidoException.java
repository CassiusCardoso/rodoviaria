package br.com.rodoviaria.spring_clean_arch.Domain.Exceptions.viagem;

public class StatusViagemInvalidoException extends RuntimeException {
    public StatusViagemInvalidoException(String message) {
        super(message);
    }
}
