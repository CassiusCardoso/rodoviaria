package br.com.rodoviaria.spring_clean_arch.application.ports.out.senha;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Administrador;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;

public interface TokenServicePort {
    String gerarToken(Passageiro passageiro);
    String gerarToken(Administrador administrador);


    String validarToken(String token);
}
