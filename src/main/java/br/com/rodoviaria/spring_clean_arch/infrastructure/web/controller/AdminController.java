package br.com.rodoviaria.spring_clean_arch.infrastructure.web.controller;

import br.com.rodoviaria.spring_clean_arch.application.dto.request.linha.AtualizarInformacoesDaLinhaRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.linha.CadastrarLinhaRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.onibus.AtualizarOnibusRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.onibus.CadastrarOnibusRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.passageiro.AtualizarInformacoesPassageiroRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.viagem.AtualizarViagemRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.request.viagem.ViagemRequest;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.linha.LinhaResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.onibus.OnibusResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.AtualizarInformacoesPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.passageiro.PassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemPorPassageiroResponse;
import br.com.rodoviaria.spring_clean_arch.application.dto.response.viagem.ViagemResponse;
import br.com.rodoviaria.spring_clean_arch.application.usecases.admin.AutenticarAdminUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.linha.AtualizarInformacoesDaLinhaUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.linha.CadastrarLinhaUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.linha.DesativarLinhaUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.onibus.AtualizarOnibusUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.onibus.CadastrarOnibusUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.onibus.DesativarOnibusUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.onibus.ListarTodosOnibusUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro.AtualizarInformacoesPassageiroUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro.BuscarInformacoesPassageiroUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.passageiro.DesativarPassageiroUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.ticket.AdminCancelarTicketUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.viagem.AtualizarViagemUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.viagem.CancelarViagemUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.viagem.CriarViagemUseCase;
import br.com.rodoviaria.spring_clean_arch.application.usecases.viagem.ListarViagensPorPassageiroUseCase;
import br.com.rodoviaria.spring_clean_arch.infrastructure.security.AdminAutenticado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AutenticarAdminUseCase autenticarAdminUseCase;
    // ONIBUS USE CASE
    private final CadastrarOnibusUseCase cadastrarOnibusUseCase;
    private final ListarTodosOnibusUseCase listarTodosOnibusUseCase;
    private final DesativarOnibusUseCase desativarOnibusUseCase;
    private final AtualizarOnibusUseCase atualizarOnibusUseCase;
    // PASSAGEIROS USE CASE
    private final AtualizarInformacoesPassageiroUseCase atualizarInformacoesPassageiroUseCase;
    private final DesativarPassageiroUseCase desativarPassageiroUseCase;
    private final BuscarInformacoesPassageiroUseCase buscarInformacoesPassageiroUseCase;

    // LINHAS USE CASE
    private final DesativarLinhaUseCase desativarLinhaUseCase;
    private final AtualizarInformacoesDaLinhaUseCase atualizarInformacoesDaLinhaUseCase;
    private final CadastrarLinhaUseCase cadastrarLinhaUseCase;

    // TICKETS USE CASE
    private final AdminCancelarTicketUseCase adminCancelarTicketUseCase;

    // VIAGEM USE CASE
    private final CriarViagemUseCase criarViagemUseCase;
    private final AtualizarViagemUseCase atualizarViagemUseCase;
    private final CancelarViagemUseCase cancelarViagemUseCase;
    private final ListarViagensPorPassageiroUseCase listarViagensPorPassageiro;

    public AdminController(AutenticarAdminUseCase autenticarAdminUseCase, CadastrarOnibusUseCase cadastrarOnibusUseCase, ListarTodosOnibusUseCase listarTodosOnibusUseCase, DesativarOnibusUseCase desativarOnibusUseCase, AtualizarOnibusUseCase atualizarOnibusUseCase, AtualizarInformacoesPassageiroUseCase atualizarInformacoesPassageiroUseCase, DesativarPassageiroUseCase desativarPassageiroUseCase, BuscarInformacoesPassageiroUseCase buscarInformacoesPassageiroUseCase, DesativarLinhaUseCase desativarLinhaUseCase, AtualizarInformacoesDaLinhaUseCase atualizarInformacoesDaLinhaUseCase, CadastrarLinhaUseCase cadastrarLinhaUseCase, AdminCancelarTicketUseCase adminCancelarTicketUseCase, CriarViagemUseCase criarViagemUseCase, AtualizarViagemUseCase atualizarViagemUseCase, CancelarViagemUseCase cancelarViagemUseCase, ListarViagensPorPassageiroUseCase listarViagensPorPassageiro){
        this.autenticarAdminUseCase = autenticarAdminUseCase;
        // ONIBUS
        this.cadastrarOnibusUseCase = cadastrarOnibusUseCase;
        this.listarTodosOnibusUseCase = listarTodosOnibusUseCase;
        this.desativarOnibusUseCase = desativarOnibusUseCase;
        this.atualizarOnibusUseCase = atualizarOnibusUseCase;
        // PASSAGEIRO
        this.atualizarInformacoesPassageiroUseCase = atualizarInformacoesPassageiroUseCase;
        this.desativarPassageiroUseCase = desativarPassageiroUseCase;
        this.buscarInformacoesPassageiroUseCase = buscarInformacoesPassageiroUseCase;

        // LINHAS
        this.desativarLinhaUseCase = desativarLinhaUseCase;
        this.atualizarInformacoesDaLinhaUseCase = atualizarInformacoesDaLinhaUseCase;
        this.cadastrarLinhaUseCase = cadastrarLinhaUseCase;

        // TICKET
        this.adminCancelarTicketUseCase = adminCancelarTicketUseCase;

        // VIAGEM
        this.criarViagemUseCase = criarViagemUseCase;
        this.atualizarViagemUseCase = atualizarViagemUseCase;
        this.cancelarViagemUseCase = cancelarViagemUseCase;
        this.listarViagensPorPassageiro = listarViagensPorPassageiro;
    }

    @PostMapping("/onibus")
    public ResponseEntity<OnibusResponse> cadastrarOnibus(@RequestBody CadastrarOnibusRequest request, @AuthenticationPrincipal AdminAutenticado adminAutenticado ){ // << A anotação agora pega o "crachá" de Admin
        // O Controller vai orquestar: receber o DTO de request e passa para o caso de uso
        OnibusResponse onibusResponse = cadastrarOnibusUseCase.execute(request);
        // A verificação de role não é mais necessária aqui!
        // O Spring Security já garantiu que só um admin chegaria neste método.

        // O corpo do método fica mais limpo, focado em sua tarefa principal.
        // Retornar um ResponseEntity com o dto de response
        return ResponseEntity.status(HttpStatus.CREATED).body(onibusResponse);
    }

    @GetMapping()
    public ResponseEntity<List<OnibusResponse>> listarTodosOnibus(@AuthenticationPrincipal AdminAutenticado adminAutenticado){
        List<OnibusResponse> onibusResponses = listarTodosOnibusUseCase.execute();
        return ResponseEntity.ok(onibusResponses);
    }

    @DeleteMapping("/onibus/{onibusId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativarOnibus(@PathVariable UUID onibusId, @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        desativarOnibusUseCase.execute(onibusId);
    }

    @PutMapping("/onibus/{onibusId}") // Lógica e DTO corrigidos
    public ResponseEntity<OnibusResponse> atualizarOnibus(@PathVariable UUID onibusId, @RequestBody AtualizarOnibusRequest request, @AuthenticationPrincipal AdminAutenticado adminLogado){
        OnibusResponse onibusResponse = atualizarOnibusUseCase.execute(request, onibusId);
        return ResponseEntity.ok(onibusResponse);
    }

    // ENDPOINTS PARA GERENCIAMENTO DE PASSAGEIROS

    @GetMapping("/passageiros/{passageiroId}")
    public ResponseEntity<PassageiroResponse> buscarPassageiroPorId(@PathVariable UUID passageiroId, @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        PassageiroResponse passageiroResponse = buscarInformacoesPassageiroUseCase.execute(passageiroId);
        return ResponseEntity.ok(passageiroResponse);
    }

    @PutMapping("/passageiros/{passageiroId}")
    public ResponseEntity<AtualizarInformacoesPassageiroResponse> atualizarPassageiro(@RequestBody AtualizarInformacoesPassageiroRequest request, @PathVariable UUID passageiroId, @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        AtualizarInformacoesPassageiroResponse atualizarInformacoesPassageiroResponse = atualizarInformacoesPassageiroUseCase.execute(request, passageiroId);
        return ResponseEntity.ok(atualizarInformacoesPassageiroResponse);
    }

    @DeleteMapping("/passageiros/{passageiroId}") // URL Padronizada
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativarPassageiro(@PathVariable UUID passageiroId, @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        desativarPassageiroUseCase.execute(passageiroId);
    }


    // ENDPOINTS PARA GERENCIAMENTO DE LINHAS
    @DeleteMapping("/linha/{linhaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativarLinha(@PathVariable UUID linhaId, @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        desativarLinhaUseCase.execute(linhaId);
    }

    @PutMapping("/{linhaId}")
    public ResponseEntity<LinhaResponse> atualizarLinha(@PathVariable UUID linhaId, @RequestBody AtualizarInformacoesDaLinhaRequest request, @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        LinhaResponse linhaResponse = atualizarInformacoesDaLinhaUseCase.execute(linhaId, request);
        return ResponseEntity.ok(linhaResponse);
    }

    @PostMapping("/linha")
    public ResponseEntity<LinhaResponse> cadastrarLinha(@RequestBody CadastrarLinhaRequest request, @AuthenticationPrincipal AdminAutenticado adminAutenticado) {
        LinhaResponse linhaResponse = cadastrarLinhaUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(linhaResponse);
    }

    // GERENCIAMENTO DE TICKET
    @DeleteMapping("/tickets/{ticketId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelarTicket(@PathVariable UUID ticketId, @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        adminCancelarTicketUseCase.execute(ticketId);
    }

    // GERENCIAMENTO DE VIAGEM
    @PostMapping("/viagem")
    public ResponseEntity<ViagemResponse> criarViagem(@RequestBody ViagemRequest request, @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        ViagemResponse viagemResponse = criarViagemUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(viagemResponse);
    }

    @PutMapping("/viagem/{viagemId}")
    public ResponseEntity<ViagemResponse> atualizarViagem(@PathVariable UUID viagemId, @RequestBody AtualizarViagemRequest request, @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        ViagemResponse viagemResponse = atualizarViagemUseCase.execute(viagemId, request);
        return ResponseEntity.ok(viagemResponse);
    }

    @DeleteMapping("/viagem/{viagemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelarViagem(@PathVariable UUID viagemId, @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        cancelarViagemUseCase.execute(viagemId);
    }

    @GetMapping("/viagens/passageiros/{passageiroId}")
    public ResponseEntity<List<ViagemPorPassageiroResponse>> listarViagensPorPassageiro(@PathVariable UUID passageiroId, @AuthenticationPrincipal AdminAutenticado adminAutenticado){
        List<ViagemPorPassageiroResponse> viagensPorPassageiro = listarViagensPorPassageiro.execute(passageiroId);
        return ResponseEntity.ok(viagensPorPassageiro);
    }
}
