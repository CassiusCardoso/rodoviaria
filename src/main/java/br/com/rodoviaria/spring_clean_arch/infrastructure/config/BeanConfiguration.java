package br.com.rodoviaria.spring_clean_arch.infrastructure.config;

import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.SenhaEncoderPort;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.TokenServicePort;
import br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro.*;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.PassageiroRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.TicketRepository;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.ViagemRepository;
import br.com.rodoviaria.spring_clean_arch.infrastructure.adapters.BCryptSenhaEncoderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfiguration {

        // Você REMOVE o bean daqui.
    /*
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    */

    @Bean
    public SenhaEncoderPort senhaEncoderPort(PasswordEncoder encoder){
        return new BCryptSenhaEncoderAdapter(encoder);
    }
    @Bean
    public CadastrarPassageiroUseCase cadastrarPassageiroUseCase(
            PassageiroRepository passageiroRepository,
            SenhaEncoderPort encoder){
        return new CadastrarPassageiroUseCase(passageiroRepository, encoder);
    }
    @Bean
    public DesativarPassageiroUseCase desativarPassageiroUseCase(
            PassageiroRepository passageiroRepository){
        return new DesativarPassageiroUseCase(passageiroRepository);
    }
    @Bean
    public AtualizarInformacoesPassageiroUseCase atualizarInformacoesPassageiroUseCase(
            PassageiroRepository passageiroRepository){
        return new AtualizarInformacoesPassageiroUseCase(passageiroRepository);
    }

    @Bean
    public ListarPassageirosDeUmaViagemUseCase listarPassageirosDeUmaViagemUseCase(
            TicketRepository ticketRepository,
            ViagemRepository viagemRepository){
        return new ListarPassageirosDeUmaViagemUseCase(ticketRepository, viagemRepository);
    }
    @Bean
    public BuscarInformacoesPassageiroUseCase buscarInformacoesPassageiroUseCase(
            PassageiroRepository passageiroRepository){
        return new BuscarInformacoesPassageiroUseCase(passageiroRepository);
    }
    @Bean
    public AutenticarPassageiroUseCase autenticarPassageiroUseCase(
            PassageiroRepository passageiroRepository,
            SenhaEncoderPort senhaEncoderPort,
            TokenServicePort tokenServicePort){
        return new AutenticarPassageiroUseCase(passageiroRepository, senhaEncoderPort, tokenServicePort);
    }
}
