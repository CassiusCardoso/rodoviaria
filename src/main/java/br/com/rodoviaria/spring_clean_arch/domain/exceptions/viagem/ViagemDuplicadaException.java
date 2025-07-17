// Ex: br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem.ViagemDuplicadaException.java

package br.com.rodoviaria.spring_clean_arch.domain.exceptions.viagem;

public class ViagemDuplicadaException extends RuntimeException {
    public ViagemDuplicadaException(String message) {
        super(message);
    }
}