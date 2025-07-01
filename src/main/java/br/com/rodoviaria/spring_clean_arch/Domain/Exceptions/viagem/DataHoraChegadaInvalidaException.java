package br.com.rodoviaria.spring_clean_arch.Domain.Exceptions.viagem;

public class DataHoraChegadaInvalidaException extends RuntimeException {
    public DataHoraChegadaInvalidaException(String message) {
        super(message);
    }
}
