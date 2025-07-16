package br.com.rodoviaria.spring_clean_arch.infrastructure.config;

import br.com.rodoviaria.spring_clean_arch.domain.entities.Administrador;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.AdministradorRepository;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Email;
import br.com.rodoviaria.spring_clean_arch.domain.valueobjects.Senha;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(AdministradorRepository adminRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Defina o e-mail e a senha do seu primeiro administrador
            String adminEmail = "admin@rodoviaria.com";
            String adminSenhaPlana = "SenhaForte@123"; // Use uma senha forte

            // 1. VERIFICA SE O ADMIN JÁ EXISTE
            if (adminRepository.buscarPorEmail(adminEmail).isEmpty()) {
                System.out.println(">>> Criando primeiro administrador...");

                // 2. CODIFICA A SENHA NORMAL USANDO O ENCODER DO SPRING
                String senhaCriptografada = passwordEncoder.encode(adminSenhaPlana);

                // 3. CRIA A ENTIDADE
                Administrador primeiroAdmin = new Administrador(
                        UUID.randomUUID(),
                        "Admin Principal",
                        new Email(adminEmail),
                        Senha.carregar(senhaCriptografada), // Carrega o hash, não a senha plana
                        true
                );

                // 4. SALVA NO BANCO
                adminRepository.salvar(primeiroAdmin);
                System.out.println(">>> Administrador 'admin@rodoviaria.com' criado com sucesso!");
            } else {
                System.out.println(">>> Administrador 'admin@rodoviaria.com' já existe. Nenhuma ação necessária.");
            }
        };
    }
}