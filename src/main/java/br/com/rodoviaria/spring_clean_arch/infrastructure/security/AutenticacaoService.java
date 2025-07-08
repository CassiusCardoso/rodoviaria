package br.com.rodoviaria.spring_clean_arch.infrastructure.security;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Passageiro;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class AutenticacaoService implements UserDetailsService {

    private final PassageiroRepository passageiroRepository;
    public AutenticacaoService(PassageiroRepository passageiroRepository){
        this.passageiroRepository = passageiroRepository;
    }

    public PassageiroRepository getPassageiroRepository() {
        return passageiroRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // O "username" para o Spring será o nosso e-mail
        Optional<Passageiro> passageiroOptional = passageiroRepository.buscarPorEmail(username);

        if (passageiroOptional.isEmpty()) {
            throw new UsernameNotFoundException("Dados inválidos!");
        }

        // Se o passageiro for encontrado, nós o convertemos para o nosso UsuarioAutenticado
        // que o Spring Security consegue entender.
        Passageiro passageiro = passageiroOptional.get();
        return new UsuarioAutenticado(passageiro);
    }

}
