package br.com.rodoviaria.spring_clean_arch.domain.exception;

public class DomainException extends RuntimeException{
    public DomainException(String message){
        super(message);
    }
}
