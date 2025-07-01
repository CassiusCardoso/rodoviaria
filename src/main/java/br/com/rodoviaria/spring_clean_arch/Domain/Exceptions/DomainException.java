package br.com.rodoviaria.spring_clean_arch.Domain.Exceptions;

public class DomainException extends RuntimeException{
    public DomainException(String message){
        super(message);
    }
}
