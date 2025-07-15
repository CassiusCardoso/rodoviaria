package br.com.rodoviaria.spring_clean_arch.infrastructure.security;

import br.com.rodoviaria.spring_clean_arch.domain.repositories.AdministradorRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminAutenticacaoService implements UserDetailsService {
    private final AdministradorRepository administradorRepository;
    public AdminAutenticacaoService(AdministradorRepository administradorRepository){
        this.administradorRepository = administradorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return administradorRepository.buscarPorEmail(username)
                .map(AdminAutenticado::new) // Converte a entidade para UserDetails
                .orElseThrow(() -> new UsernameNotFoundException("Administrador não encontrado: " + username));
    }
}
