package br.com.rodoviaria.spring_clean_arch.domain.exception.viagem;

public class DataHoraChegadaInvalidaException extends RuntimeException {
    public DataHoraChegadaInvalidaException(String message) {
        super(message);
    }
}
