package br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem;

public class DataHoraChegadaInvalidaException extends RuntimeException {
    public DataHoraChegadaInvalidaException(String message) {
        super(message);
    }
}
