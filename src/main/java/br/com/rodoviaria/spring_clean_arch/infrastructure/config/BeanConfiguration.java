package br.com.rodoviaria.spring_clean_arch.infrastructure.config;

import br.com.rodoviaria.spring_clean_arch.application.mapper.*;
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
            TokenServicePort tokenServicePort,
            AdministradorMapper administradorMapper
    ) {
        return new AutenticarAdminUseCase(administradorRepository, senhaEncoderPort, tokenServicePort, administradorMapper);
    }

    @Bean
    public SenhaEncoderPort senhaEncoderPort(PasswordEncoder encoder) {
        return new BCryptSenhaEncoderAdapter(encoder);
    }

    @Bean
    public CadastrarPassageiroUseCase cadastrarPassageiroUseCase(
            PassageiroRepository passageiroRepository,
            SenhaEncoderPort encoder,
            PassageiroMapper passageiroMapper) {
        return new CadastrarPassageiroUseCase(passageiroRepository, encoder, passageiroMapper);
    }

    @Bean
    public DesativarPassageiroUseCase desativarPassageiroUseCase(
            PassageiroRepository passageiroRepository) {
        return new DesativarPassageiroUseCase(passageiroRepository);
    }

    @Bean
    public AtualizarInformacoesPassageiroUseCase atualizarInformacoesPassageiroUseCase(
            PassageiroRepository passageiroRepository,
            PassageiroMapper passageiroMapper) {
        return new AtualizarInformacoesPassageiroUseCase(passageiroRepository, passageiroMapper);
    }

    @Bean
    public ListarPassageirosDeUmaViagemUseCase listarPassageirosDeUmaViagemUseCase(
            TicketRepository ticketRepository,
            ViagemRepository viagemRepository,
            TicketMapper ticketMapper
            ) {
        return new ListarPassageirosDeUmaViagemUseCase(ticketRepository, viagemRepository, ticketMapper);
    }

    @Bean
    public BuscarInformacoesPassageiroUseCase buscarInformacoesPassageiroUseCase(
            PassageiroRepository passageiroRepository,
            PassageiroMapper passageiroMapper) {
        return new BuscarInformacoesPassageiroUseCase(passageiroRepository, passageiroMapper);
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
            ViagemRepository viagemRepository,
            OnibusMapper onibusMapper
    ) {
        return new AtualizarOnibusUseCase(onibusRepository, viagemRepository, onibusMapper);
    }

    @Bean
    public BuscarInformacoesOnibusUseCase buscarInformacoesOnibusUseCase(
            OnibusRepository onibusRepository,
            OnibusMapper onibusMapper
    ) {
        return new BuscarInformacoesOnibusUseCase(onibusRepository, onibusMapper);
    }

    @Bean
    public CadastrarOnibusUseCase cadastrarOnibusUseCase(
            OnibusRepository onibusRepository,
            OnibusMapper onibusMapper
    ) {
        return new CadastrarOnibusUseCase(onibusRepository, onibusMapper);
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
            OnibusRepository onibusRepository,
            OnibusMapper onibusMapper
    ) {
        return new ListarTodosOnibusUseCase(onibusRepository, onibusMapper);
    }

    // Linha
    @Bean
    public BuscarViagensPorRotaUseCase buscarInformacoesDaLinhaUseCase(
            LinhaRepository linhaRepository,
            ViagemRepository viagemRepository,
            ViagemMapper viagemMapper
    ) {
        return new BuscarViagensPorRotaUseCase(linhaRepository, viagemRepository, viagemMapper);
    }

    @Bean
    public AtualizarInformacoesDaLinhaUseCase atualizarInformacoesDaLinhaUseCase(
            LinhaRepository linhaRepository,
            LinhaMapper linhaMapper) {
        return new AtualizarInformacoesDaLinhaUseCase(linhaRepository, linhaMapper);
    }

    @Bean
    public ListarTodasLinhasUseCase listarTodasLinhasUseCase(
            LinhaRepository linhaRepository,
            LinhaMapper linhaMapper
    ) {
        return new ListarTodasLinhasUseCase(linhaRepository, linhaMapper);
    }

    @Bean
    public DesativarLinhaUseCase desativarLinhaUseCase(LinhaRepository linhaRepository) {
        return new DesativarLinhaUseCase(linhaRepository);
    }

    @Bean
    public BuscarLinhaPorOrigemDestinoUseCase buscarLinhaPorOrigemDestinoUseCase(
            LinhaRepository linhaRepository,
            LinhaMapper linhaMapper
    ) {
        return new BuscarLinhaPorOrigemDestinoUseCase(linhaRepository, linhaMapper);
    }

    @Bean
    public CadastrarLinhaUseCase cadastarLinhaUseCase(LinhaRepository linhaRepository, LinhaMapper linhaMapper) {
        return new CadastrarLinhaUseCase(linhaRepository, linhaMapper);
    }

    @Bean
    public BuscarLinhaPorIdUseCase buscarLinhaPorIdUseCase(LinhaRepository linhaRepository, LinhaMapper linhaMapper) {
        return new BuscarLinhaPorIdUseCase(linhaRepository, linhaMapper);
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
    public AtualizarTicketUseCase atualizarTicketUseCase(TicketRepository ticketRepository, TicketMapper ticketMapper) {
        return new AtualizarTicketUseCase(ticketRepository, ticketMapper);
    }

    @Bean
    public ComprarTicketUseCase comprarTicketUseCase(
            ViagemRepository viagemRepository,
            TicketRepository ticketRepository,
            PassageiroRepository passageiroRepository,
            TicketMapper ticketMapper) {
        return new ComprarTicketUseCase(viagemRepository, ticketRepository, passageiroRepository, ticketMapper);
    }

    @Bean
    public ListarMeusTicketsUseCase listarMeusTicketsUseCase(TicketRepository ticketRepository, TicketMapper ticketMapper) {
        return new ListarMeusTicketsUseCase(ticketRepository, ticketMapper);
    }

    @Bean
    public BuscarDetalhesDoTicketUseCase buscarDetalhesDoTicketUseCase(TicketRepository ticketRepository, TicketMapper ticketMapper) {
        return new BuscarDetalhesDoTicketUseCase(ticketRepository, ticketMapper);
    }

    // Viagem
    @Bean
    public AtualizarViagemUseCase atualizarViagemUseCase(ViagemRepository viagemRepository, OnibusRepository onibusRepository, ViagemMapper viagemMapper) {
        return new AtualizarViagemUseCase(viagemRepository, onibusRepository, viagemMapper);
    }

    @Bean
    public BuscarViagensDisponiveisUseCase buscarViagensDisponiveisUseCase(ViagemRepository viagemRepository, ViagemMapper viagemMapper) {
        return new BuscarViagensDisponiveisUseCase(viagemRepository, viagemMapper);
    }

    @Bean
    public CancelarViagemUseCase cancelarViagemUseCase(ViagemRepository viagemRepository){
        return new CancelarViagemUseCase(viagemRepository);
    }

    @Bean
    public ListarViagensPorPassageiro listarViagensPorPassageiro(ViagemRepository viagemRepository, PassageiroRepository passageiroRepository, ViagemMapper viagemMapper){
        return new ListarViagensPorPassageiro(viagemRepository, passageiroRepository, viagemMapper);
    }

    @Bean
    public CriarViagemUseCase criarViagemUseCase(ViagemRepository viagemRepository, OnibusRepository onibusRepository, LinhaRepository linhaRepository, ViagemMapper viagemMapper){
        return new CriarViagemUseCase(viagemRepository, linhaRepository, onibusRepository, viagemMapper);
    }
    @Bean
    public BuscarViagensPorLinhaUseCase buscarViagensPorLinhaUseCase(ViagemRepository viagemRepository, LinhaRepository linhaRepository, ViagemMapper viagemMapper){
        return new BuscarViagensPorLinhaUseCase(viagemRepository, linhaRepository, viagemMapper);
    }
}


