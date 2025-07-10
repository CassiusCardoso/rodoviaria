package br.com.rodoviaria.spring_clean_arch.infrastructure.security;

import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.adapters.JwtTokenServiceAdapter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final JwtTokenServiceAdapter tokenService;
    private final PassageiroRepository passageiroRepository;

    public SecurityFilter(JwtTokenServiceAdapter tokenService, PassageiroRepository passageiroRepository) {
        this.tokenService = tokenService;
        this.passageiroRepository = passageiroRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recuperarToken(request);
        if (token != null) {
            // Valida o token e pega o email (subject)
            var email = tokenService.validarToken(token);
            // CORREÇÃO: Faça a busca e depois converta (mapeie) o resultado
            passageiroRepository.buscarPorEmail(email)
                    .ifPresent(passageiro -> { // Usa ifPresent para tratar o Optional de forma segura
                        // Cria o "adaptador" UserDetails a partir da sua entidade de domínio
                        UserDetails usuario = new UsuarioAutenticado(passageiro);

                        var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
        }
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        // Remove o prefixo "Bearer " do token
        return authHeader.replace("Bearer ", "");
    }

}
