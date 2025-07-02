package br.com.rodoviaria.spring_clean_arch.domain.exceptions;

public class DomainException extends RuntimeException{
    public DomainException(String message){
        super(message);
    }
}
