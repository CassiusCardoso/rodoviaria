package br.com.rodoviaria.spring_clean_arch.Domain.Exceptions.ticket;

public class FormaPagamentoInvalidaException extends RuntimeException {
    public FormaPagamentoInvalidaException(String message) {
        super(message);
    }
}
