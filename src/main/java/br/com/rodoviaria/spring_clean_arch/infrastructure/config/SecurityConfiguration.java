package br.com.rodoviaria.spring_clean_arch.infrastructure.config;

import br.com.rodoviaria.spring_clean_arch.domain.repositories.AdministradorRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.adapters.JwtTokenServiceAdapter;
import br.com.rodoviaria.spring_clean_arch.infrastructure.security.AdminSecurityFilter;
import br.com.rodoviaria.spring_clean_arch.infrastructure.security.SecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    // --- Beans de Filtro ---
    @Bean
    public SecurityFilter securityFilter(JwtTokenServiceAdapter tokenService, PassageiroRepository repository) {
        return new SecurityFilter(tokenService, repository);
    }

    @Bean
    public AdminSecurityFilter adminSecurityFilter(JwtTokenServiceAdapter tokenService, AdministradorRepository administradorRepository) {
        return new AdminSecurityFilter(tokenService, administradorRepository);
    }

    // --- Cadeias de Segurança ---

    @Bean
    @Order(1) // Prioridade 1: Regras do Admin são verificadas primeiro
    public SecurityFilterChain adminFilterChain(HttpSecurity http, AdminSecurityFilter adminSecurityFilter) throws Exception {
        return http
                .securityMatcher("/admin/**") // Aplica este filtro APENAS para rotas /admin/**
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/admin/login").permitAll() // Permite login de admin
                        .anyRequest().hasAuthority("ROLE_ADMIN") // Qualquer outra rota /admin/** exige a role ADMIN
                )
                .addFilterBefore(adminSecurityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain appFilterChain(HttpSecurity http, SecurityFilter securityFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/passageiros/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/passageiros").permitAll()
                        .requestMatchers(HttpMethod.GET, "/linhas/**").permitAll()
                        // REMOVIDO: .requestMatchers("/admin/login").permitAll()
                        // Já é tratado pela cadeia adminFilterChain
                        // Qualquer outra requisição precisa de autenticação (de passageiro)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}