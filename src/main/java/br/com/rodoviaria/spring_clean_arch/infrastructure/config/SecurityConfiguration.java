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

    // --- Beans de Filtro REMOVIDOS ---
    // A anotação @Bean foi removida dos métodos abaixo para
    // evitar o registro global dos filtros.

    @Bean
    @Order(1)
    public SecurityFilterChain adminFilterChain(HttpSecurity http, JwtTokenServiceAdapter tokenService, AdministradorRepository administradorRepository) throws Exception {
        // O filtro agora é instanciado aqui dentro
        AdminSecurityFilter adminSecurityFilter = new AdminSecurityFilter(tokenService, administradorRepository);
        return http
                .securityMatcher("/admin/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/admin/login").permitAll()
                        .anyRequest().hasAuthority("ROLE_ADMIN")
                )
                .addFilterBefore(adminSecurityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain appFilterChain(HttpSecurity http, JwtTokenServiceAdapter tokenService, PassageiroRepository repository) throws Exception {
        // O filtro agora é instanciado aqui dentro
        SecurityFilter securityFilter = new SecurityFilter(tokenService, repository);
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos (incluindo o Swagger, como corrigido anteriormente)
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/passageiros/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/passageiros").permitAll()
                        .requestMatchers(HttpMethod.GET, "/linhas/**").permitAll()
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