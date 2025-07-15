package br.com.rodoviaria.spring_clean_arch.infrastructure.security;

import br.com.rodoviaria.spring_clean_arch.application.usecases.admin.AutenticarAdminUseCase;
import br.com.rodoviaria.spring_clean_arch.infrastructure.security.AdminAutenticado;import jakarta.servlet.FilterChain;
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

        String path = request.getRequestURI();
        if (path.equals("/admin/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            AdminAutenticado admin = autenticarAdminUseCase.autenticar(token);

            if (admin != null) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
