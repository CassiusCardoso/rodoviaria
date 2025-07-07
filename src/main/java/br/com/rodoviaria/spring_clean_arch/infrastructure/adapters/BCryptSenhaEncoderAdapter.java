package br.com.rodoviaria.spring_clean_arch.infrastructure.adapters;

import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.SenhaEncoderPort;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BCryptSenhaEncoderAdapter implements SenhaEncoderPort {
    private final PasswordEncoder encoder;
    public BCryptSenhaEncoderAdapter(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    // Contrato encode
    @Override
    public String encode(String senha){
        return encoder.encode(senha);
    }

    // Contrato matches
    @Override
    public boolean matches(String senhaBruta, String senhaCodificada){
        return encoder.matches(senhaBruta, senhaCodificada);
    }


}
