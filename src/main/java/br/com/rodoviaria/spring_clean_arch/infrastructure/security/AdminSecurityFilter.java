package br.com.rodoviaria.spring_clean_arch.infrastructure.security;

import br.com.rodoviaria.spring_clean_arch.domain.repositories.AdministradorRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.adapters.JwtTokenServiceAdapter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AdminSecurityFilter extends OncePerRequestFilter {

    private final JwtTokenServiceAdapter tokenService;
    private final AdministradorRepository administradorRepository;

    public AdminSecurityFilter(JwtTokenServiceAdapter tokenService, AdministradorRepository administradorRepository) {
        this.tokenService = tokenService;
        this.administradorRepository = administradorRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = recuperarToken(request);
        if (token != null && !token.isEmpty()) {
            try {
                var email = tokenService.validarToken(token);
                // Verifica se o email não é nulo ou vazio
                if (email != null && !email.isEmpty()) {
                    administradorRepository.buscarPorEmail(email)
                            .filter(admin -> admin.getAtivo()) // Verifica se está ativo
                            .ifPresent(administrador -> {
                                UserDetails usuario = new AdminAutenticado(administrador);
                                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                            });
                }
            } catch (Exception e) {
                // Log do erro se necessário, mas não bloqueia a requisição
                // Token inválido será tratado como não autenticado
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}