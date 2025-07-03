package br.com.rodoviaria.spring_clean_arch.application.ports.out.senha;

public interface SenhaEncoderPort {
    String encode(String senha);
    boolean matches(String senhaBruta, String senhaCodificada);
}
