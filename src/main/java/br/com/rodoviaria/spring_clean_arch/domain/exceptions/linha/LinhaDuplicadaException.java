package br.com.rodoviaria.spring_clean_arch.domain.exceptions.linha;

public class LinhaDuplicadaException extends RuntimeException {
    public LinhaDuplicadaException(String message) {
        super(message);
    }
}
