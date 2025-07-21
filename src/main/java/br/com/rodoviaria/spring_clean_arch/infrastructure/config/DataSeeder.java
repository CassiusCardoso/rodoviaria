// Arquivo: DataSeeder.java

package br.com.rodoviaria.spring_clean_arch.infrastructure.config;

import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.SenhaEncoderPort;
import br.com.rodoviaria.spring_clean_arch.domain.entities.Administrador;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.AdministradorRepository;
// Importe as classes Email e Senha
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Senha;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(AdministradorRepository adminRepository, SenhaEncoderPort senhaEncoderPort) {
        return args -> {
            String adminEmail = "admin@rodoviaria.com";
            if (adminRepository.buscarPorEmail(adminEmail).isEmpty()) {
                System.out.println(">>> Criando primeiro administrador...");

                // CORREÇÃO: Use o método de fábrica estático, passando a implementação do port.
                Administrador primeiroAdmin = Administrador.criarNovo(
                        "Admin Principal",
                        adminEmail,
                        "SenhaForte@123",
                        senhaEncoderPort
                );

                adminRepository.salvar(primeiroAdmin);
                System.out.println(">>> Administrador '" + adminEmail + "' criado com sucesso!");
            } else {
                System.out.println(">>> Administrador '" + adminEmail + "' já existe. Nenhuma ação necessária.");
            }
        };
    }
}