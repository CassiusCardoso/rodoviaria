package br.com.rodoviaria.spring_clean_arch.infrastructure.security;

import jakarta.servlet.FilterChain;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import br.com.rodoviaria.spring_clean_arch.application.usecases.admin.AutenticarAdminUseCase;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AdminSecurityFilter extends OncePerRequestFilter {

    private final AutenticarAdminUseCase autenticarAdminUseCase;

    public AdminSecurityFilter(AutenticarAdminUseCase autenticarAdminUseCase) {
        this.autenticarAdminUseCase = autenticarAdminUseCase;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            AdminAutenticado admin = autenticarAdminUseCase.autenticar(token);

            if (admin != null) {
                // ---> INÍCIO DA CORREÇÃO <---
                // Avisa ao Spring Security que este usuário está autenticado
                var authentication = new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // ---> FIM DA CORREÇÃO <---
            }
        }

        // Continua a execução da cadeia de filtros
        filterChain.doFilter(request, response);
    }
}
