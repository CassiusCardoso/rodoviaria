package br.com.rodoviaria.spring_clean_arch.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                // Desativa a proteção CSRF, comum em APIs stateless (sem sessão)
                .csrf(csrf -> csrf.disable())
                // Configura a política de sessão para ser stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configura as regras de autorização para cada endpoint
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso público aos endpoints de login e cadastro
                        .requestMatchers(HttpMethod.POST, "/passageiros/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/passageiros").permitAll()
                        // Para qualquer outra requisição, o usuário precisa estar autenticado
                        .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // Bean necessário para o Spring gerenciar a autenticação
        return configuration.getAuthenticationManager();
    }

    // Você já tem este bean em BeanConfiguration.java, pode mantê-lo lá ou movê-lo para cá.
    // É comum manter todas as configurações de segurança juntas.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
