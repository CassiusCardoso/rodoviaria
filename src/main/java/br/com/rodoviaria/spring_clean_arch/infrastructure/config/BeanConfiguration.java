package br.com.rodoviaria.spring_clean_arch.infrastructure.config;

import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.SenhaEncoderPort;
import br.com.rodoviaria.spring_clean_arch.application.ports.out.senha.TokenServicePort;
import br.com.rodoviaria.spring_clean_arch.application.usecases.admin.AutenticarAdminUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.linha.*;
import br.com.rodoviaria.spring_clean_arch.application.usecases.onibus.*;
import br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro.*;
import br.com.rodoviaria.spring_clean_arch.application.usecases.ticket.*;
import br.com.rodoviaria.spring_clean_arch.application.usecases.viagem.*;
import br.com.rodoviaria.spring_clean_arch.domain.repositories.*;
import br.com.rodoviaria.spring_clean_arch.infrastructure.adapters.BCryptSenhaEncoderAdapter;
import br.com.rodoviaria.spring_clean_arch.infrastructure.security.AdminAutenticaoService;
import br.com.rodoviaria.spring_clean_arch.infrastructure.security.AutenticacaoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.beans.BeanProperty;

@Configuration
public class BeanConfiguration {

    // == BEANS DE SEGURANÇA ==
    @Bean
    public AutenticacaoService autenticacaoService(PassageiroRepository passageiroRepository) {
        return new AutenticacaoService(passageiroRepository);
    }

    @Bean
    public AdminAutenticaoService adminAutenticaoService(AdministradorRepository administradorRepository) {
        return new AdminAutenticaoService(administradorRepository);
    }

    // ADMIN
    @Bean
    public AutenticarAdminUseCase autenticarAdminUseCase(
            AdministradorRepository administradorRepository,
            SenhaEncoderPort senhaEncoderPort,
            TokenServicePort tokenServicePort
    ) {
        return new AutenticarAdminUseCase(administradorRepository, senhaEncoderPort, tokenServicePort);
    }

    @Bean
    public SenhaEncoderPort senhaEncoderPort(PasswordEncoder encoder) {
        return new BCryptSenhaEncoderAdapter(encoder);
    }

    @Bean
    public CadastrarPassageiroUseCase cadastrarPassageiroUseCase(
            PassageiroRepository passageiroRepository,
            SenhaEncoderPort encoder) {
        return new CadastrarPassageiroUseCase(passageiroRepository, encoder);
    }

    @Bean
    public DesativarPassageiroUseCase desativarPassageiroUseCase(
            PassageiroRepository passageiroRepository) {
        return new DesativarPassageiroUseCase(passageiroRepository);
    }

    @Bean
    public AtualizarInformacoesPassageiroUseCase atualizarInformacoesPassageiroUseCase(
            PassageiroRepository passageiroRepository) {
        return new AtualizarInformacoesPassageiroUseCase(passageiroRepository);
    }

    @Bean
    public ListarPassageirosDeUmaViagemUseCase listarPassageirosDeUmaViagemUseCase(
            TicketRepository ticketRepository,
            ViagemRepository viagemRepository) {
        return new ListarPassageirosDeUmaViagemUseCase(ticketRepository, viagemRepository);
    }

    @Bean
    public BuscarInformacoesPassageiroUseCase buscarInformacoesPassageiroUseCase(
            PassageiroRepository passageiroRepository) {
        return new BuscarInformacoesPassageiroUseCase(passageiroRepository);
    }

    @Bean
    public AutenticarPassageiroUseCase autenticarPassageiroUseCase(
            PassageiroRepository passageiroRepository,
            SenhaEncoderPort senhaEncoderPort,
            TokenServicePort tokenServicePort) {
        return new AutenticarPassageiroUseCase(passageiroRepository, senhaEncoderPort, tokenServicePort);
    }

    // Ônibus
    @Bean
    public AtualizarOnibusUseCase atualizarOnibusUseCase(
            OnibusRepository onibusRepository,
            ViagemRepository viagemRepository
    ) {
        return new AtualizarOnibusUseCase(onibusRepository, viagemRepository);
    }

    @Bean
    public BuscarInformacoesOnibusUseCase buscarInformacoesOnibusUseCase(
            OnibusRepository onibusRepository
    ) {
        return new BuscarInformacoesOnibusUseCase(onibusRepository);
    }

    @Bean
    public CadastrarOnibusUseCase cadastrarOnibusUseCase(
            OnibusRepository onibusRepository
    ) {
        return new CadastrarOnibusUseCase(onibusRepository);
    }

    @Bean
    public DesativarOnibusUseCase desativarOnibusUseCase(
            OnibusRepository onibusRepository,
            ViagemRepository viagemRepository
    ) {
        return new DesativarOnibusUseCase(onibusRepository, viagemRepository);
    }

    @Bean
    public ListarTodosOnibusUseCase listarTodosOnibusUseCase(
            OnibusRepository onibusRepository
    ) {
        return new ListarTodosOnibusUseCase(onibusRepository);
    }

    // Linha
    @Bean
    public BuscarViagensPorRotaUseCase buscarInformacoesDaLinhaUseCase(
            LinhaRepository linhaRepository,
            ViagemRepository viagemRepository
    ) {
        return new BuscarViagensPorRotaUseCase(linhaRepository, viagemRepository);
    }

    @Bean
    public AtualizarInformacoesDaLinhaUseCase atualizarInformacoesDaLinhaUseCase(
            LinhaRepository linhaRepository) {
        return new AtualizarInformacoesDaLinhaUseCase(linhaRepository);
    }

    @Bean
    public ListarTodasLinhasUseCase listarTodasLinhasUseCase(
            LinhaRepository linhaRepository
    ) {
        return new ListarTodasLinhasUseCase(linhaRepository);
    }

    @Bean
    public DesativarLinhaUseCase desativarLinhaUseCase(LinhaRepository linhaRepository) {
        return new DesativarLinhaUseCase(linhaRepository);
    }

    @Bean
    public BuscarLinhaPorOrigemDestinoUseCase buscarLinhaPorOrigemDestinoUseCase(
            LinhaRepository linhaRepository
    ) {
        return new BuscarLinhaPorOrigemDestinoUseCase(linhaRepository);
    }

    @Bean
    public CadastrarLinhaUseCase cadastarLinhaUseCase(LinhaRepository linhaRepository) {
        return new CadastrarLinhaUseCase(linhaRepository);
    }

    @Bean
    public BuscarLinhaPorIdUseCase buscarLinhaPorIdUseCase(LinhaRepository linhaRepository) {
        return new BuscarLinhaPorIdUseCase(linhaRepository);
    }

    @Bean
    public AdminCancelarTicketUseCase adminCancelarTicketUseCase(TicketRepository ticketRepository) {
        return new AdminCancelarTicketUseCase(ticketRepository);
    }

    @Bean
    public PassageiroCancelarTicketUseCase passageiroCancelarTicketUseCase(TicketRepository ticketRepository) {
        return new PassageiroCancelarTicketUseCase(ticketRepository);
    }

    @Bean
    public AtualizarTicketUseCase atualizarTicketUseCase(TicketRepository ticketRepository) {
        return new AtualizarTicketUseCase(ticketRepository);
    }

    @Bean
    public ComprarTicketUseCase comprarTicketUseCase(
            ViagemRepository viagemRepository,
            TicketRepository ticketRepository,
            PassageiroRepository passageiroRepository) {
        return new ComprarTicketUseCase(viagemRepository, ticketRepository, passageiroRepository);
    }

    @Bean
    public ListarMeusTicketsUseCase listarMeusTicketsUseCase(TicketRepository ticketRepository) {
        return new ListarMeusTicketsUseCase(ticketRepository);
    }

    @Bean
    public BuscarDetalhesDoTicketUseCase buscarDetalhesDoTicketUseCase(TicketRepository ticketRepository) {
        return new BuscarDetalhesDoTicketUseCase(ticketRepository);
    }

    // Viagem
    @Bean
    public AtualizarViagemUseCase atualizarViagemUseCase(ViagemRepository viagemRepository, OnibusRepository onibusRepository) {
        return new AtualizarViagemUseCase(viagemRepository, onibusRepository);
    }

    @Bean
    public BuscarViagensDisponiveisUseCase buscarViagensDisponiveisUseCase(ViagemRepository viagemRepository) {
        return new BuscarViagensDisponiveisUseCase(viagemRepository);
    }

    @Bean
    public CancelarViagemUseCase cancelarViagemUseCase(ViagemRepository viagemRepository){
        return new CancelarViagemUseCase(viagemRepository);
    }

    @Bean
    public ListarViagensPorPassageiro listarViagensPorPassageiro(ViagemRepository viagemRepository, PassageiroRepository passageiroRepository){
        return new ListarViagensPorPassageiro(viagemRepository, passageiroRepository);
    }

    @Bean
    public CriarViagemUseCase criarViagemUseCase(ViagemRepository viagemRepository, OnibusRepository onibusRepository, LinhaRepository linhaRepository){
        return new CriarViagemUseCase(viagemRepository, linhaRepository, onibusRepository);
    }
    @Bean
    public BuscarViagensPorLinhaUseCase buscarViagensPorLinhaUseCase(ViagemRepository viagemRepository, LinhaRepository linhaRepository){
        return new BuscarViagensPorLinhaUseCase(viagemRepository, linhaRepository);
    }
}


