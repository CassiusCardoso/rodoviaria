package br.com.rodoviaria.spring_clean_arch.domain.Exceptions.ticket;

public class FormaPagamentoInvalidaException extends RuntimeException {
    public FormaPagamentoInvalidaException(String message) {
        super(message);
    }
}
