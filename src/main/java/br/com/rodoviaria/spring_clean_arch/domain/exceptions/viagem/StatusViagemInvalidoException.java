package br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem;

public class StatusViagemInvalidoException extends RuntimeException {
    public StatusViagemInvalidoException(String message) {
        super(message);
    }
}
