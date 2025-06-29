package br.com.rodoviaria.spring_clean_arch.domain.exception.ticket;

public class FormaPagamentoInvalidaException extends RuntimeException {
    public FormaPagamentoInvalidaException(String message) {
        super(message);
    }
}
